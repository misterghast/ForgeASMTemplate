package org.hypbase.asmtemplate;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.MethodNode;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClassTransformer implements IClassTransformer {
    Logger logger = LogManager.getLogger("ASM");

    public ClassTransformer() {

    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(transformedName.equals("net.minecraft.client.Minecraft")) { //change this to the class you want to inject into
            return patch(basicClass);
        }
        return basicClass;
    }

    private byte[] patch(byte[] basicClass) {
        //basically stolen from ResourceLoader
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        MethodNode changedMethod = null;

        for(Object mn : classNode.methods) {
            if(mn instanceof MethodNode) {
                MethodNode mmn = (MethodNode) mn;
                if(mmn.name.equals(MCPNaming.method(""))) { //put srg method name here that you want to inject into
                    changedMethod = mmn;
                }
            }
        }

        if (changedMethod != null) {
            logger.log(Level.DEBUG, " - Found refreshResources 1/3");

            for(int i = 0; i < changedMethod.instructions.size(); i++) {
                AbstractInsnNode ain = changedMethod.instructions.get(i);
                if(ain instanceof MethodInsnNode) {
                    MethodInsnNode min = (MethodInsnNode) ain;
                    if(min.name.equals(MCPNaming.method("func_110541_a"))) {
                        //InsnList toInsert = new InsnList();
                        //toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "", "", "", false)); //inject this method (first argument is the class with method that contains injected code, second argument is method to inject into the vanilla method's name, third is that method's arguments)
                        // toInsert.add(new VarInsnNode(Opcodes.ALOAD, 1));

                        //changedMethod.instructions.insertBefore(min, toInsert);
                        logger.log(Level.DEBUG, " - Patched refreshResources 3/3");

                        i+=2;
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);

        return writer.toByteArray();
    }

    private byte[] patchDummyClass(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        logger.log(Level.INFO, "Found dummy class: " + classNode.name);

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);

        return writer.toByteArray();
    }
}

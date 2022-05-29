package org.hypbase.asmtemplate;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

public class LoadingPlugin implements IFMLLoadingPlugin {

    public static boolean IN_MCP = false;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{org.hypbase.asmtemplate.ClassTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        IN_MCP = !(Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}

package com.andreyprofdev.protractorendtoendtestsrunner.protractor;

public class NodePathResolver {

    private static final String NODE_EXECUTABLE_WINDOWS = "\\node\\node.exe";
    private static final String NODE_EXECUTABLE_DEFAULT = "/node/node";

    enum OS { Windows, Mac, Linux, SunOS;

        public static OS guess() {
            final String osName = System.getProperty("os.name");
            return  osName.contains("Windows") ? OS.Windows :
                    osName.contains("Mac") ? OS.Mac :
                            osName.contains("SunOS") ? OS.SunOS :
                                    OS.Linux;
        }
    }

    private final String nodeInstallDirectory;

    public NodePathResolver(String nodeInstallDirectory) {
        this.nodeInstallDirectory = nodeInstallDirectory;
    }

    private boolean isWindows(){
        return OS.guess() == OS.Windows;
    }

    public String getNodePath(){
        return nodeInstallDirectory + (isWindows() ? NODE_EXECUTABLE_WINDOWS : NODE_EXECUTABLE_DEFAULT);
    }
}

package org.cneko.toneko.common.util.scheduled;

import org.jetbrains.annotations.NotNull;

public class SchedulerPoolProvider {
    private static final ISchedulerPool INSTANCE;

    private static boolean tryLoadClass(String name){
        try {
            Class.forName(name);
        } catch (ClassNotFoundException ignored){
            return false;
        }
        return true;
    }

    static {
        Object warpped = null;

        try {
            if (tryLoadClass("io.papermc.paper.threadedregions.RegionizedServer")) {
                warpped = Class.forName("org.cneko.toneko.bukkit.util.FoliaSchedulerPoolImpl").getDeclaredConstructor().newInstance();
            } else if (tryLoadClass("org.bukkit.Server")) {
                warpped = Class.forName("org.cneko.toneko.bukkit.util.BukkitSchedulerPool").getDeclaredConstructor().newInstance();
            } else if (tryLoadClass("org.cneko.toneko.fabric.ToNeko")) {
                warpped = Class.forName("org.cneko.toneko.fabric.util.FabricSchedulerPoolImpl").getDeclaredConstructor().newInstance();
            }
        } catch (Throwable ex) {
            throw new RuntimeException("could not init schedule pool", ex);
        }

        if (!(warpped instanceof ISchedulerPool)){
            throw new RuntimeException("could not init schedule pool");
        }

        INSTANCE = (ISchedulerPool)warpped;
    }

    @NotNull
    public static ISchedulerPool getINSTANCE() {
        return INSTANCE;
    }
}

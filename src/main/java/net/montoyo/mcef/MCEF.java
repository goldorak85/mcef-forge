package net.montoyo.mcef;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.montoyo.mcef.client.ClientProxy;
import net.montoyo.mcef.easy_forge_compat.Configuration;
import net.montoyo.mcef.utilities.Log;

@Mod("forgecef")
public class MCEF {

    public static final String VERSION = "1.33";
    public static boolean ENABLE_EXAMPLE;
    public static boolean SKIP_UPDATES;
    public static boolean WARN_UPDATES;
    public static boolean USE_FORGE_SPLASH;
    public static String FORCE_MIRROR = null;
    public static String HOME_PAGE;
    public static String[] CEF_ARGS = new String[0];
    public static boolean CHECK_VRAM_LEAK;
    public static boolean SHUTDOWN_JCEF;
    public static boolean SECURE_MIRRORS_ONLY;
    public static boolean DEBUG;

    public static MCEF INSTANCE;

    public static BaseProxy PROXY = DistExecutor.<BaseProxy>runForDist(() -> ClientProxy::new, () -> BaseProxy::new);

    public MCEF() {
        MCEF.debug("MCEF Initalizing...");
        Log.info("Loading MCEF config...");
        Configuration cfg = new Configuration();

        INSTANCE = this;

        //Config: main
        SKIP_UPDATES = cfg.getBoolean("skipUpdates", "main", false, "Do not update binaries.");
        WARN_UPDATES = cfg.getBoolean("warnUpdates", "main", true, "Tells in the chat if a new version of MCEF is available (broken)."); // TODO: Find equavilent of playerjoinevent for fabric
        USE_FORGE_SPLASH = cfg.getBoolean("useForgeSplash", "main", false, "(No effect) Use Forge's splash screen to display resource download progress (may be unstable).");
        CEF_ARGS = cfg.getString("cefArgs", "main", "", "Command line arguments passed to CEF. For advanced users.").split("\\s+");
        SHUTDOWN_JCEF = cfg.getBoolean("shutdownJcef", "main", false, "Set this to true if your Java process hangs after closing Minecraft. This is disabled by default because it makes the launcher think Minecraft crashed...");
        SECURE_MIRRORS_ONLY = cfg.getBoolean("secureMirrorsOnly", "main", true, "Only enable secure (HTTPS) mirror. This should be kept to true unless you know what you're doing.");
        DEBUG = cfg.getBoolean("debug", "main", false, "Enable debug print.");

        String mirror = cfg.getString("forcedMirror", "main", "", "A URL that contains every MCEF resources; for instance https://montoyo.net/jcef.").trim();
        if (mirror.length() > 0)
            FORCE_MIRROR = mirror;

        //Config: exampleBrowser
        ENABLE_EXAMPLE = cfg.getBoolean("enable", "exampleBrowser", false, "Set this to false if you don't want to enable the F10 browser.");
        HOME_PAGE = cfg.getString("home", "exampleBrowser", "https://www.vsynctester.com/testing/mouse.html", "The home page of the F10 browser.");

        //Config: debug
        CHECK_VRAM_LEAK = cfg.getBoolean("checkForVRAMLeak", "debug", false, "Track allocated OpenGL textures to make sure there's no leak");
        cfg.save();

        PROXY.onPreInit();
        this.onInit(); // old init

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                onMinecraftShutdown();
            }
        }));
    }

    public void onInit() {
        PROXY.onInit();
    }

    public static void onMinecraftShutdown() {
        PROXY.onShutdown();
    }

    public static void debug(String message) {
        if (DEBUG)
            System.out.println(message);
    }

}

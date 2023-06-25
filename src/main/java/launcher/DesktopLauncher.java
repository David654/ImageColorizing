package launcher;

import application.Application;

import com.formdev.flatlaf.FlatDarkLaf;

public class DesktopLauncher
{
    public static void main(String[] args)
    {
        FlatDarkLaf.setup();

        Application application = new Application();
        application.launch();
    }
}
package edu.kis.powp.jobs2d;

import edu.kis.legacy.drawer.panel.DefaultDrawerFrame;
import edu.kis.legacy.drawer.panel.DrawPanelController;
import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.drivers.adapter.Job2DToDrawPanelAdapter;
import edu.kis.powp.jobs2d.drivers.command.DriverCommandFactory;
import edu.kis.powp.jobs2d.events.SelectChangeVisibleOptionListener;
import edu.kis.powp.jobs2d.events.SelectTestFigureOptionListener;
import edu.kis.powp.jobs2d.features.DrawerFeature;
import edu.kis.powp.jobs2d.features.DriverFeature;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestJobs2dPatterns {

	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Setup test concerning preset figures in context.
	 *
	 * @param application Application context.
	 */
	private static void setupPresetTests(Application application) {
		SelectTestFigureOptionListener selectTestFigureOptionListener = new SelectTestFigureOptionListener(
				DriverFeature.getDriverManager());

		application.addTest(SelectTestFigureOptionListener.FIGURE_JOE_1,
				selectTestFigureOptionListener);
		application.addTest(SelectTestFigureOptionListener.FIGURE_JOE_2,
				selectTestFigureOptionListener);
		application.addTest("Create Square", e -> {
			DriverCommandFactory.createSquare().execute(
					DriverFeature.getDriverManager().getCurrentDriver()
			);
		});
		application.addTest("Create Big Square", e -> {
			DriverCommandFactory.createBigSquare().execute(
					DriverFeature.getDriverManager().getCurrentDriver()
			);
		});
		application.addTest("Create Triangle", e -> {
			DriverCommandFactory.createTriangle().execute(
					DriverFeature.getDriverManager().getCurrentDriver()
			);
		});
	}

	/**
	 * Setup driver manager, and set default driver for application.
	 *
	 * @param application Application context.
	 */
	private static void setupDrivers(Application application) {
		Job2dDriver loggerDriver = new LoggerDriver();
		DriverFeature.addDriver("Logger Driver", loggerDriver);
		DriverFeature.getDriverManager().setCurrentDriver(loggerDriver);

		Job2dDriver testDriver = new Job2DToDrawPanelAdapter(DrawerFeature.getDrawerController());
		DriverFeature.addDriver("Buggy Simulator", testDriver);

		DriverFeature.updateDriverInfo();
	}

	/**
	 * Auxiliary routines to enable using Buggy Simulator.
	 *
	 * @param application Application context.
	 */
	private static void setupDefaultDrawerVisibilityManagement(Application application) {
		DefaultDrawerFrame defaultDrawerWindow = DefaultDrawerFrame.getDefaultDrawerFrame();
		application.addComponentMenuElementWithCheckBox(DrawPanelController.class,
				"Default Drawer Visibility",
				new SelectChangeVisibleOptionListener(defaultDrawerWindow), true);
		defaultDrawerWindow.setVisible(true);
	}

	/**
	 * Setup menu for adjusting logging settings.
	 *
	 * @param application Application context.
	 */
	private static void setupLogger(Application application) {
		application.addComponentMenu(Logger.class, "Logger", 0);
		application.addComponentMenuElement(Logger.class, "Clear log",
				(ActionEvent e) -> application.flushLoggerOutput());
		application.addComponentMenuElement(Logger.class, "Fine level",
				(ActionEvent e) -> logger.setLevel(Level.FINE));
		application.addComponentMenuElement(Logger.class, "Info level",
				(ActionEvent e) -> logger.setLevel(Level.INFO));
		application.addComponentMenuElement(Logger.class, "Warning level",
				(ActionEvent e) -> logger.setLevel(Level.WARNING));
		application.addComponentMenuElement(Logger.class, "Severe level",
				(ActionEvent e) -> logger.setLevel(Level.SEVERE));
		application.addComponentMenuElement(Logger.class, "OFF logging",
				(ActionEvent e) -> logger.setLevel(Level.OFF));
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			Application app = new Application("2d jobs Visio");
			DrawerFeature.setupDrawerPlugin(app);

			DriverFeature.setupDriverPlugin(app);
			setupDrivers(app);
			setupPresetTests(app);
			setupLogger(app);

			app.setVisibility(true);
		});
	}

}

package edu.kis.powp.jobs2d;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.kis.legacy.drawer.shape.LineFactory;
import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.commands.ComplexCommand;
import edu.kis.powp.jobs2d.commands.ShapeFactory;
import edu.kis.powp.jobs2d.drivers.adapter.AbstractDriverAdapter;
import edu.kis.powp.jobs2d.drivers.adapter.DrawerAdapter;
import edu.kis.powp.jobs2d.drivers.adapter.LineDrawerAdapter;
import edu.kis.powp.jobs2d.events.SelectTestFigureOptionListener;
import edu.kis.powp.jobs2d.features.DrawerFeature;
import edu.kis.powp.jobs2d.features.DriverFeature;
import edu.kis.powp.jobs2d.magicpresets.FiguresJane;
import edu.kis.powp.jobs2d.magicpresets.FiguresJoe;

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

		SelectTestFigureOptionListener selectTestFigureOptionListener1 =
				new SelectTestFigureOptionListener(DriverFeature.getDriverManager()){
			@Override
			public void actionPerformed(ActionEvent e) {
				FiguresJoe.figureScript2(DriverFeature.getDriverManager().getCurrentDriver());
			}
		};

		final AbstractDriverAdapter janeAdapter = new AbstractDriverAdapter(0, 0, DriverFeature.getDriverManager());


		application.addTest("Figure Joe 1", selectTestFigureOptionListener);
		application.addTest("Figure Joe 2", selectTestFigureOptionListener1);
		application.addTest("Figure Jane", e -> { FiguresJane.figureScript(janeAdapter); });
		application.addTest("Rectangle", e -> {
			ComplexCommand commands = ShapeFactory.rectangle(DriverFeature.getDriverManager().getCurrentDriver());
			commands.execute();
		});

		application.addTest("Triangle", e -> {
			ComplexCommand commands = ShapeFactory.triangle(DriverFeature.getDriverManager().getCurrentDriver());
			commands.execute();
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

		Job2dDriver testDriver = new DrawerAdapter(DrawerFeature.getDrawerController());
		DriverFeature.addDriver("Buggy Simulator - basic line", testDriver);

		Job2dDriver testDriver1 = new LineDrawerAdapter(DrawerFeature.getDrawerController(), LineFactory.getSpecialLine());
		DriverFeature.addDriver("Buggy Simulator - special line", testDriver1);

		Job2dDriver testDriver2 = new LineDrawerAdapter(DrawerFeature.getDrawerController(), LineFactory.getDottedLine());
		DriverFeature.addDriver("Buggy Simulator - dotted line", testDriver2);

		DriverFeature.updateDriverInfo();
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
		application.addComponentMenuElement(Logger.class, "Fine level", (ActionEvent e) -> logger.setLevel(Level.FINE));
		application.addComponentMenuElement(Logger.class, "Info level", (ActionEvent e) -> logger.setLevel(Level.INFO));
		application.addComponentMenuElement(Logger.class, "Warning level",
				(ActionEvent e) -> logger.setLevel(Level.WARNING));
		application.addComponentMenuElement(Logger.class, "Severe level",
				(ActionEvent e) -> logger.setLevel(Level.SEVERE));
		application.addComponentMenuElement(Logger.class, "OFF logging", (ActionEvent e) -> logger.setLevel(Level.OFF));
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Application app = new Application("2d jobs Visio");
				DrawerFeature.setupDrawerPlugin(app);

				DriverFeature.setupDriverPlugin(app);
				setupDrivers(app);
				setupPresetTests(app);
				setupLogger(app);

				app.setVisibility(true);
			}
		});
	}

}

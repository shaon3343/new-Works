
public class FinacleMTParsing {

	Timer timer;
	
	String mtFilePath = "";
	static Connection conn;
	static String textFile = "";
	static String url;
	

	public void finacleMTParserScheduler() {
		Logger logger = Logger.getLogger("LCAlertLog");
		timer = new Timer();
		Long timing = 30*60L; // In second
		try{
			timing= Long.parseLong(AppConstants.props.get("SCHEDULER_TIMING"));

		}catch(Exception e){
			//System.out.println("EXCEPTION IN PARSING SCHEDULER TIMER !");
			logger.warn("EXCEPTION IN PARSING SCHEDULER TIMER !");
			e.printStackTrace();
		}
		logger.info("####### SCHEDULER TIMING : "+timing+" second");
		//System.out.println("####### SCHEDULER TIMING : "+timing+" second");
		timer.schedule(new FinacleMTParser(), 0, timing * 1000);
	}
}

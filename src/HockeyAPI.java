import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class HockeyAPI {	
	public static void main(String[] args) throws IOException, ParseException, InterruptedException {
		Map<String, String> teamAbbr = new HashMap<>();
		teamAbbr.put("ANA","Pacific");
		teamAbbr.put("ARI","Central");
		teamAbbr.put("BOS", "Atlantic");
		teamAbbr.put("BUF", "Atlantic");
		teamAbbr.put("CGY", "Pacific");
		teamAbbr.put("CAR", "Metro");
		teamAbbr.put("CHI", "Central");
		teamAbbr.put("COL", "Central");
		teamAbbr.put("CBJ", "Metro");
		teamAbbr.put("DAL", "Central");
		teamAbbr.put("DET", "Atlantic");
		teamAbbr.put("EDM", "Pacific");
		teamAbbr.put("FLA", "Atlantic");
		teamAbbr.put("LAK", "Pacific");
		teamAbbr.put("MIN", "Central");
		teamAbbr.put("MTL", "Atlantic");
		teamAbbr.put("NSH", "Central");
		teamAbbr.put("NJD", "Metro");
		teamAbbr.put("NYI", "Metro");
		teamAbbr.put("NYR", "Metro");
		teamAbbr.put("OTT", "Atlantic");
		teamAbbr.put("PHI", "Metro");
		teamAbbr.put("PIT", "Metro");
		teamAbbr.put("SJS", "Pacific");
		teamAbbr.put("SEA", "Pacific");
		teamAbbr.put("STL", "Central");
		teamAbbr.put("TBL", "Atlantic");
		teamAbbr.put("TOR", "Atlantic");
		teamAbbr.put("VAN", "Pacific");
		teamAbbr.put("VGK", "Pacific");
		teamAbbr.put("WSH", "Metro");
		teamAbbr.put("WPG", "Central");
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("Hockey Player Database");
		XSSFRow row;
		Map<String,Object[]> playerData = new TreeMap<String,Object[]>();
		int rowCounter = 1;
		for(String team : teamAbbr.keySet()) {
			System.out.println(team + ":");
			Thread.sleep(1000);
			URL url = new URL("https://api-web.nhle.com/v1/roster/" + team + "/current");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int response = conn.getResponseCode();
			if(response != 200) {
				throw new RuntimeException("HTTPResponseCode " + response);
			}
			else {
				StringBuilder infoString = new StringBuilder();
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext()) {
					infoString.append(sc.nextLine());
				}
				sc.close();
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(String.valueOf(infoString));
				ArrayList<JSONArray>positions = new ArrayList<>();
				positions.add((JSONArray) obj.get("forwards"));
				positions.add((JSONArray) obj.get("defensemen"));
				positions.add((JSONArray) obj.get("goalies"));
				for(JSONArray pos : positions) {
					for(Object player : pos) {
						JSONObject currPlayer = ((JSONObject) player);
						JSONObject currPlayerFirstName = (JSONObject) currPlayer.get("firstName");
						JSONObject currPlayerLastName = (JSONObject) currPlayer.get("lastName");
						long currPlayerNum = (long) currPlayer.get("sweaterNumber");
						String currPlayerPos = (String) currPlayer.get("positionCode");
						if (currPlayerPos.equals("R")) {
							currPlayerPos = "RW";
						}
						else if (currPlayerPos.equals("L")) {
							currPlayerPos = "LW";
						}
						String currPlayerCountry = (String) currPlayer.get("birthCountry");
						String currPlayerHanded = (String) currPlayer.get("shootsCatches");
						String playerFullName = currPlayerFirstName.get("default").toString() + " " +  currPlayerLastName.get("default").toString();
						String currPlayerDOB = (String) currPlayer.get("birthDate");
						int currPlayerYearOB = Integer.parseInt(currPlayerDOB.substring(0,4));
						int currPlayerMonthOB = Integer.parseInt(currPlayerDOB.substring(5,7));
						int currPlayerDayOB = Integer.parseInt(currPlayerDOB.substring(8,10));
						LocalDate birthday = LocalDate.of(currPlayerYearOB, currPlayerMonthOB, currPlayerDayOB);
						LocalDate today = LocalDate.now();
						Period ageObj = Period.between(birthday, today);
						int age = ageObj.getYears();
						String ageStr = Integer.toString(age);
						playerData.put(Integer.toString(rowCounter), new Object[] {playerFullName, currPlayerPos, team, currPlayerCountry, Long.toString(currPlayerNum), ageStr, currPlayerHanded, teamAbbr.get(team)});
						rowCounter++;
					}
				}			
			}
			int rowid = 0;
			Set<String> keyid = playerData.keySet();
			for(String key : keyid) {
				row = spreadsheet.createRow(rowid++);
				Object[] playerInfo = playerData.get(key);
				int cellid = 0;
				for(Object obj : playerInfo) {
					Cell cell = row.createCell(cellid++);
					cell.setCellValue((String) obj);	
				}
			}
			
			FileOutputStream excelFile = new FileOutputStream(new File("C:\\Users\\Admin\\Downloads\\WordlePlayerDatabase.xlsx"));
			workbook.write(excelFile);
			excelFile.close();
			
			
		}
		
	}

}

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class Mainbot {

	public static void main(String[] args) {
	
		TelegramBot bot = TelegramBotAdapter.build("905318150:AAEWFutCykgyHPS-rmcARGUfzwb-OdjvtiI");
		GetUpdatesResponse updatesResponse;
		SendResponse sendResponse;
		BaseResponse baseResponse;
		String resposta;
		int m = 0;
		while (true) {
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));
			List<Update> updates = updatesResponse.updates();
			for (Update update : updates) {
				
				m = update.updateId() + 1;

				System.out.println("Recebendo mensagem: "+update.message().text());
				
				logMsg(update.message().text().toString());
				
				resposta = analisaMsg(update.message().text().toString());
				
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				
				System.out.println("Resposta de chatAction enviada ? "+baseResponse.isOk());
				
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(),resposta));
				
				System.out.println("Mensagem enviada...?"+sendResponse.isOk());
				
			}
		}		
	}
	static String analisaMsg(String msg) {

		if (msg.contains("?")){
			return formataResposta(msg);			
		}
		return "Nao entendi...Como posso te ajudar ?";
	}

	static String formataResposta(String msg) {

		String s2[] = msg.split(" ");
		for(int i=0;i<s2.length;i++) {
			System.out.println(s2[i]);
		}
			
		return s2[s2.length-1];
	}
	
	static void logMsg(String str) {
	
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/db_teste?user=root&password=mysqleox");
			Statement s = con.createStatement(); 
			String sql = "INSERT INTO logtelmsg (message, datadora) VALUES (?,?)";
			java.sql.PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, str);
			stmt.setString(2, LocalDateTime.now().toString());
			stmt.execute();
			
			stmt.close(); s.close(); con.close();			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}				
	
			
	}
	
	
}





import org.example.models.ConnectDB;
import org.example.models.UserModels.*;
import org.example.models.UserLoginFunctions;
import org.example.models.MenuAndInterface;
import org.example.models.DatabaseFunctionsUserRelated;
import java.sql.Connection;

void main(){

    ConnectDB sqliteDB = new ConnectDB();
    Connection connection = sqliteDB.getConnection();

    try {
        while (true) {

            int choose = MenuAndInterface.menu();

            if (choose == 0) {
                MenuAndInterface.closeProgramMessage();
                return;
            }

            if (choose == 1) {

                Login userLog = UserLoginFunctions.createNewLogin();
                String logged = DatabaseFunctionsUserRelated.checkIfHaveAccount(userLog, connection);

                if (logged != null) {

                    while (true){
                        int loggedChoose =  MenuAndInterface.loggedMenu();

                        if (loggedChoose == 0){
                            MenuAndInterface.closeProgramMessage();
                            break;
                        }

                        else if (loggedChoose == 1){
                            userLog = UserLoginFunctions.userPasswordReset(userLog,  connection);
                        }

                        else if (loggedChoose == 2) {
                            DatabaseFunctionsUserRelated.printUserInfo(userLog, connection);
                        }
                    }
                    break;
                }
            }
            else if (choose == 2) {
                User user = UserLoginFunctions.createNewUser();
                DatabaseFunctionsUserRelated.insertUserDataBase(user, connection);
            }
        }
    }
    finally {
        sqliteDB.closeConnection();
    }
}

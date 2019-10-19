package clientlib;

import com.dojo.tank.solver.BasicSolver;

public class ClientApp {

    final static String URL = "http://dojorena.io/codenjoy-contest/board/player/zwef9wxfk60zs3zxbkia?code=7523947537256409738&gameName=battlecity";
    final static String TEST_URL = "http://10.22.19.165:8080/codenjoy-contest/board/player/rbye7jraqg9ouwyeg6hp?code=8499226565780091758&gameName=battlecity";

    public static void main(String[] args) {
        try {
            WebSocketRunner.run(URL, new BasicSolver());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package clientlib;

import com.dojo.tank.solver.BasicSolver;

public class ClientApp {

    private final static String URL = "http://dojorena.io/codenjoy-contest/board/player/zwef9wxfk60zs3zxbkia?code=7523947537256409738&gameName=battlecity";

    public static void main(String[] args) {
        try {
            WebSocketRunner.run(URL, new BasicSolver());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

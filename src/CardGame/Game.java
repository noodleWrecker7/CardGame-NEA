package CardGame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;


public class Game {

    List<String> AUTH_LIST; // stores all the valid user names
    List<String> AVAILABLE_CARDS; // stores the available cards in the stack
    Player playerOne; // object for player one
    Player playerTwo; // object for player two


    private List<String> getAuthList(String file) {
        List<String> list = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) { // each line in the file
                list.add(line.toLowerCase()); // adds to returned list
            }
            reader.close();
        } catch (Exception e) {
            System.out.print("ERROR: ");
            System.out.println(e);
            System.exit(1); // exits program
        }
        return list;
    }

    private String authorise(String name) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 3; i++) { // gives them three tries
            System.out.print(name + ", Enter your name: ");
            String input = sc.nextLine();
            if (AUTH_LIST.contains(input.toLowerCase())) { // if their name is valid
                System.out.println("Welcome " + input + ", you are authorised to play the game.");
                return input; // returns their name
            }
            System.out.println("Incorrect");
        }
        System.out.println("You have had too many tries, exiting...");
        System.exit(1);
        return null;
    }

    public Game() {
        AUTH_LIST = getAuthList("auth_list.txt");
        playerOne = new Player(authorise("Player 1"));
        playerTwo = new Player(authorise("Player 2"));

        AVAILABLE_CARDS = createCardList();

        play();
    }


    private List<String> createCardList() {
        String[] orderedCards = {"r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8", "r9", "r10", "b1", "b2", "b3", "b4",
                "b5", "b6", "b7", "b8", "b9", "b10", "y1", "y2", "y3", "y4", "y5", "y6", "y7", "y8", "y9", "y10"};
        List<String> orderedList = new ArrayList<>(Arrays.asList(orderedCards));
        List<String> newList = new ArrayList<>();
        Random rand = new Random();
        while (orderedList.size() > 0) {
            int indx = rand.nextInt(orderedList.size());
            newList.add(orderedList.get(indx));
            orderedList.remove(indx);
        }
        return newList;
    }

    private void soutBigLine() {
        for (int i = 0; i < 69; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    private void play() {
        System.out.println("STARTING GAME...");
        soutBigLine();

        for (int i = 1; i <= 15; i++) {
            soutBigLine();
            System.out.println("ROUND " + i);
            soutBigLine();

            // Playing for player one
            System.out.println(playerOne.getName() + "'s turn");
            playerTurn(playerOne);
            soutBigLine();
            // Playing for player two
            System.out.println(playerTwo.getName() + "'s turn");
            playerTurn(playerTwo);
            soutBigLine();
            // Finding winner
            int winnerNum = findRoundWinner(playerOne.getCurrentCard(), playerTwo.getCurrentCard());
            if (winnerNum == 1) {
                // Add to p1's collection
                System.out.println(playerOne.getName() + "'s " + convertCardToReadable(playerOne.getCurrentCard()) + " won the round");
                playerOne.addToCollection(playerOne.getCurrentCard());
                playerOne.addToCollection(playerTwo.getCurrentCard());
                System.out.println("Their score is now " + playerOne.getScore());
            } else if (winnerNum == 2) {
                // Add to p2's collection
                System.out.println(playerTwo.getName() + "'s " + convertCardToReadable(playerTwo.getCurrentCard()) + " won the round");
                playerTwo.addToCollection(playerOne.getCurrentCard());
                playerTwo.addToCollection(playerTwo.getCurrentCard());
                System.out.println("Their score is now " + playerTwo.getScore());
            }
        }

        Player winner;
        if (playerOne.getScore() > playerTwo.getScore()) {
            winner = playerOne;
        } else {
            winner = playerTwo;
        }

        System.out.println(winner.getName() + " has won");
        String[][] leaderboard = updateLeaderBoard(winner);
        for (int i = 0; i < 5; i++) {
            if (leaderboard[i] == null) break;


        }


        System.out.println();
    }

    public static String[][] updateLeaderBoard(Player player) {
        String[][] leaderBoard = new String[6][2];

        try {
            // Reads leaderboard and stores to the leaderBoard[] array
            BufferedReader reader = new BufferedReader(new FileReader("scores.txt"));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < 5) {
                leaderBoard[count] = line.split(",");
                count++;
            }
            // Adds the player to the array

            leaderBoard[count][0] = player.getName();
            leaderBoard[count][1] = Integer.toString(player.getScore());
            count++;


            // Bubble sorts the array
            for (int i = 0; i < count - 1; i++) { // each pass
                for (int j = 0; j < count - 1 - i; j++) { // each individual number
                    System.out.println(leaderBoard[j][1]);
                    if (Integer.parseInt(leaderBoard[j][1]) < Integer.parseInt(leaderBoard[j + 1][1])) { // if needs swapping
                        String[] temp = leaderBoard[j];
                        leaderBoard[j] = leaderBoard[j + 1];
                        leaderBoard[j + 1] = temp;
                    }
                }
            }

            // Writes the highest 5 scores to the leaderboard
            BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt", false));
            for (int i = 0; i < 5; i++) {// only writes top 5
                if (leaderBoard[i][0] == null || leaderBoard[i][1] == null) continue;
                writer.write(leaderBoard[i][0] + "," + leaderBoard[i][1] + "\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
            for (int i = 0; i < e.getStackTrace().length; i++) {
                System.out.println(e.getStackTrace()[i]);
            }
        }
        return leaderBoard;
    }

    private int findRoundWinner(String cardOne, String cardTwo) {
        int cardOneValue = Integer.parseInt(cardOne.substring(1));
        int cardTwoValue = Integer.parseInt(cardTwo.substring(1));
        String cardOneColour = cardOne.substring(0, 1);
        String cardTwoColour = cardTwo.substring(0, 1);

        if (cardOneColour.equals(cardTwoColour)) {
            if (cardOneValue > cardTwoValue)
                return 1;
            else
                return 2;
        }

        if (cardOneColour.equals("r")) {
            if (cardTwoColour.equals("b"))
                return 1; // 1's red beats 2's black
            else
                return 2; // 2's yellow beats 1's red
        }
        if (cardOneColour.equals("b")) {
            if (cardTwoColour.equals("y"))
                return 1; // 1's black beats 2's yellow
            else
                return 2; // 2's red beats 1's black
        }
        if (cardOneColour.equals("y")) {
            if (cardTwoColour.equals("r"))
                return 1; // 1's black beats 2's yellow
            else
                return 2; // 2's red beats 1's black
        }

        return 0; // error
    }

    Scanner scan = new Scanner(System.in);

    private void playerTurn(Player player) {
        System.out.println(player.getName() + ", Your current score is " + player.getScore()); // telling current score
        System.out.println("Press enter to pick a card...");
        scan.nextLine(); // wait for user to enter
        player.setCurrentCard(pickCardFromDeck()); // sets players card
        System.out.println(player.getName() + ", You have picked up a " + convertCardToReadable(player.getCurrentCard()));
        System.out.println("Press enter to end your turn...");
        scan.nextLine();
    }

    private String convertCardToReadable(String card) {
        String result = "";
        if (card.substring(0, 1).equals("r")) {
            result += "Red ";
        } else if (card.substring(0, 1).equals("y")) {
            result += "Yellow ";
        } else if (card.substring(0, 1).equals("b")) {
            result += "Black ";
        }
        result += card.substring(1);
        return result;
    }

    private String pickCardFromDeck() {
        return AVAILABLE_CARDS.remove(0);
    }

    public static void main(String[] args) {
        Game game = new Game();

    }
}
// new
/*
New nEw = new New(new neW(new NEw(new nEW(new NeW()))));
class New {
    public New(neW x) {

    }
}
class neW{
    public neW(NEw x) {

    }
}
class NEw{
    public NEw(nEW x) {

    }
}
class nEW{
    public nEW(NeW x) {

    }
}
class NeW {

}
*/
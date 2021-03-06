import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Controller {

    @FXML
    private GridPane board;

    @FXML
    private ImageView turn;

    protected static final Image
            xImage = new Image("file:rcs/xImage.png"),
            oImage = new Image("file:rcs/oImage.png");

    private ImageView[] cells;

    private boolean player1Turn, won, tie;

    public boolean onPress(int row, int col) {
        if(won)
            return false;

        ImageView node = cells[row * 3 + col];
        if(node.getImage() == null) {
            Image img = player1Turn ? xImage : oImage;
            node.setImage(img);

            if(hasWon(img)) {
                won = true;
                if(tie) {
                    // alert of tie, failure of both players
                    alertTie();
                    return true;
                }
                // alert of winnings, winner is [player1Turn ? xImage : oImage]
                alertWin();
            } else {
                player1Turn = !player1Turn;
                turn.setImage(player1Turn ? xImage : oImage);
            }
            return true;
        }

        // allow players to figure out what the heck they did (i.e. pick a better spot)

        return false;
    }

    private boolean hasWon(Image image) {
        if(equalsCells(image, 0, 3, 6)
           || equalsCells(image, 1, 4, 7)
           || equalsCells(image, 2, 5, 8)
           || equalsCells(image, 0, 1, 2)
           || equalsCells(image, 3, 4, 5)
           || equalsCells(image, 6, 7, 8)
           || equalsCells(image, 0, 4, 8)
           || equalsCells(image, 2, 4, 6) ) // you win!
            return true;

        for(int i = 0; i < 9; i++)
            if(cells[i].getImage() != null) {
                if (i == 8)
                    return tie = true;
            } else break;
        return false;
    }

    private void alertWin() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You Win!");
        alert.setHeaderText("Player " + (player1Turn ? 1 : 2) + " Wins!");
        alert.setGraphic(new ImageView(player1Turn ? xImage : oImage));
        alert.setContentText("Congratulations!");
        alert.showAndWait();
    }

    /**
     * Only needed when networking is established. TODO.
     */
    private void alertLose() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You Lose!");
        alert.setHeaderText("Player " + (player1Turn ? 1 : 2) + " Wins!");
        alert.setGraphic(new ImageView(player1Turn ? xImage : oImage));
        alert.setContentText("Better luck next time!");
        alert.showAndWait();
    }

    private void alertTie() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tie!");
        alert.setHeaderText("Everybody Loses.");
        alert.setContentText("Better luck next time!");
        alert.showAndWait();
    }

    private boolean equalsCells(Image image, int... indices) {
        for(int i : indices)
            if(cells[i].getImage() == null || !cells[i].getImage().equals(image))
                return false;
        return true;
    }

    @FXML
    public void reset() {
        for(ImageView cell : cells)
            cell.setImage(null);

        player1Turn = true;
        tie = false;
        won = false;

        turn.setImage(xImage);
    }

    @FXML
    public void initialize() {
        player1Turn = true;
        tie = false;
        won = false;

        turn.setImage(xImage);

        cells = new ImageView[9];
        ObservableList<Node> list = board.getChildren();

        for(int i = 0; i < 9; i++) {
            Node node = list.get(i);
            cells[i] = (ImageView) node;
            cells[i].setImage(null);
            final int num = i;
            node.setOnMouseClicked(x -> onPress(num / 3, num % 3));
        }
    }
}

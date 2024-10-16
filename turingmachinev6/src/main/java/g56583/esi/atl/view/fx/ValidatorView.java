package g56583.esi.atl.view.fx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

/**
 * ValidatorView represents a visual component that displays a robot image,
 * a validator image, and a text field for inputting clues related to the validator.
 */
public class ValidatorView extends VBox {

    /**
     * Constructs a ValidatorView with the specified robot and validator images,
     * and a text field for clues.
     *
     * @param robotImagePath     Path to the robot image.
     * @param validatorImagePath Path to the validator image.
     */
    public ValidatorView(String robotImagePath, String validatorImagePath) {

        // Create and configure the ImageView for the robot
        ImageView robotView = new ImageView(new Image(robotImagePath));
        robotView.setPreserveRatio(true); // Maintain the aspect ratio
        robotView.setFitHeight(50); // Set the height of the robot image

        // Create and configure the ImageView for the validator
        ImageView validatorView = new ImageView(new Image(validatorImagePath));
        validatorView.setPreserveRatio(true); // Maintain the aspect ratio
        validatorView.setFitHeight(205); // Set the height of the validator image


        // Add all components to the VBox
        this.getChildren().addAll(robotView, validatorView);

        // Align all components to the top center of the VBox
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(5); // Set the spacing between the components in the VBox
    }

}

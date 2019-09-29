package red.medusa.markdownpanel.Integrate_to_javafx_demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import red.medusa.markdownpanel.model.MKFileModel;
import red.medusa.markdownpanel.view.MarkdownPanelFragment;
import tornadofx.FX;

import java.io.File;

public class MarkdownPanelForJavaFXDemo extends Application {

    /**
     * 第一步准备markdown文件并将该文件注入MKFileModel
     */
    public MarkdownPanelForJavaFXDemo() {
        File markdownFile = new File(MarkdownPanelForJavaFXDemo.class.getResource("/data/markdown-file/mk/B.md").getFile());
        MKFileModel fileModel = FX.find(MKFileModel.class);
        fileModel.setItem(markdownFile);
    }

    @Override
    public void start(Stage stage) {

        Label label = new Label(TEXT);
        label.setWrapText(true);
        label.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 20; -fx-text-fill: darkred;");

        Button button = new Button("创建MarkdownPanel");
        button.setStyle(
                "-fx-font-family: \"Comic Sans MS\"; " +
                        "-fx-font-size: 20; " +
                        "-fx-text-fill: #dc1002;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-color: #dd7f03;-fx-cursor:hand"
        );
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(e -> display());

        VBox layout = new VBox();
        layout.setSpacing(50.0);
        layout.setStyle("-fx-background-color: #ffcd80; -fx-padding: 10;");
        layout.getChildren().setAll(label, button);

        stage.setTitle("Markdown Panel Demo");
        stage.setScene(new Scene(layout, 600.0, 300.0));
        stage.show();
    }

    private void display() {
        Stage window = new Stage();
        window.initModality(Modality.NONE);
        window.setTitle("MarkdownPanel");
        window.setMinWidth(250);

        /**
         * 第二步获得MarkdownPanelFragment并得到它的ScrollPane
         */
        ScrollPane scrollPane = (ScrollPane) FX.find(MarkdownPanelFragment.class).getRoot();
        Scene scene = new Scene(scrollPane);
        // 这里主要是为了使用MarkdownPanelFragment自带的样式
        FX.Companion.applyStylesheetsTo(scene);

        /**
         * 使用自定义样式覆盖
         */
//        FX.Companion.getStylesheets().clear();
        scene.getStylesheets().addAll("/styles/MarkdownPanelForJavaFXDemo.css");

        window.setScene(scene);
        window.show();
    }

    private static final String TEXT = " MarkdownPanel与你的JavaFX应用集成非常简单,只需要两个步骤。\n\n" +
            "第一步:第一步准备markdown文件并将该文件注入MKFileModel \n" +
            "第二步:获得MarkdownPanelFragment并得到它的ScrollPane";

    public static void main(String[] args) {
        Application.launch(args);
    }
}

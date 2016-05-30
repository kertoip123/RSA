import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Created by piotrek on 04.05.2016.
 */
public class TextAreaHandler extends StreamHandler {

    private TextArea mTextArea;

    public TextAreaHandler(TextArea textArea){
        this.mTextArea = textArea;
        setLevel(Level.FINE);
    }

    @Override
    public void publish(final LogRecord record) {
        Platform.runLater(() -> {

            Date date = new Date(record.getMillis());
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String dateFormatted = formatter.format(date);

            StringWriter text = new StringWriter();
            PrintWriter out = new PrintWriter(text);
            out.println(dateFormatted + " " + record.getMessage());
            mTextArea.appendText(text.toString());
         });

    }


}

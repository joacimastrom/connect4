package joacim.connectfour.Game;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;

import org.w3c.dom.Text;

import joacim.connectfour.R;

/**
 * Created by joacim on 28/01/16.
 */
public class Audit extends Activity {

    private String audit;
    TextView auditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auditlog);
        getActionBar().hide();

        // Loads audit log
        audit = PreferenceManager.getDefaultSharedPreferences(this).getString("audit", "No Data");
        auditText = (TextView) findViewById(R.id.auditText);
        auditText.setText(audit);
        auditText.setMovementMethod(new ScrollingMovementMethod());
    }

    // Clears the audit history
    public void clearAudit(View view) {
        audit = "";
        auditText.setText((audit));
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("audit", audit).commit();

    }
}

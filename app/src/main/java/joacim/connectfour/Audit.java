package joacim.connectfour;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;

import org.w3c.dom.Text;

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


        audit = PreferenceManager.getDefaultSharedPreferences(this).getString("audit", "No Data");

        auditText = (TextView) findViewById(R.id.auditText);
        auditText.setText(audit);
        auditText.setMovementMethod(new ScrollingMovementMethod());


    }


    public void clearAudit(View view) {
        audit = "";
        auditText.setText((audit));
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("audit", audit).commit();

    }
}

package de.baumann.browser.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import de.baumann.browser.R;
import de.baumann.browser.fragment.Fragment_settings_Delete;
import de.baumann.browser.unit.BrowserUnit;
import de.baumann.browser.unit.HelperUnit;

public class Settings_Delete extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HelperUnit.initTheme(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings_delete);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new Fragment_settings_Delete())
                .commit();

        Button button = findViewById(R.id.profileListAdd);
        button.setOnClickListener(v -> {
            LinearLayout root = findViewById(R.id.root);
            Snackbar snackbarBottom = Snackbar.make(root, R.string.hint_database, Snackbar.LENGTH_SHORT);
            HelperUnit.makeSnackbarRound(snackbarBottom);
            snackbarBottom.setAction(this.getString(R.string.app_ok), (r -> BrowserUnit.clearBrowserData(this)));
            snackbarBottom.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) finish();
        if (menuItem.getItemId() == R.id.menu_help) {
            Uri webpage = Uri.parse("https://codeberg.org/Gaukler_Faun/FOSS_Browser/wiki/Delete");
            BrowserUnit.intentURL(this, webpage);
        }
        return true;
    }
}
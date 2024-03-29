package com.learn.secretmessages;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.learn.secretmessages.databinding.ActivityMainBinding;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText txtIn;
    EditText txtOut;
    EditText txtKey;
    SeekBar sb;
    Button btn;
    Button btnMove;
    //private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    public String encode( String message, int keyVal ){
        String output = "";
        char key = (char) keyVal;
        for ( int x = 0; x < message.length(); x++ ) {
            char input = message.charAt(x);
            if (input >= 'A' && input <= 'Z')
            {
                input += key;
                if (input > 'Z')
                    input -= 26;
                if (input < 'A')
                    input += 26;
            }
            else if (input >= 'a' && input <= 'z')
            {
                input += key;
                if (input > 'z')
                    input -= 26;
                if (input < 'a')
                    input += 26;
            }
            else if (input >= '0' && input <= '9')
            {
                input += (keyVal % 10);
                if (input > '9')
                    input -= 10;
                if (input < '0')
                    input += 10;
            }
            output += input;
        }
        return output;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        txtIn = findViewById(R.id.txtIn);
        txtOut = findViewById(R.id.txtOut);
        txtKey = findViewById(R.id.txtKey);
        sb = findViewById(R.id.seekBar);
        btn = findViewById(R.id.button);
        btnMove = findViewById(R.id.moveUp);
        Intent receivedIntent = getIntent();
        String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
        if (receivedText != null)
            txtIn.setText(receivedText);


        btn.setOnClickListener(view -> {
            int key = Integer.parseInt(txtKey.getText().toString());
            sb.setProgress(key + 13);
            String message = txtIn.getText().toString();
            String output = encode(message, key);
            txtOut.setText(output);
        });

        btnMove.setOnClickListener(view -> {
            txtIn.setText(txtOut.getText().toString());
            sb.setProgress(26-(sb.getProgress()));
            txtKey.setText("" + (sb.getProgress() - 13));
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int key = sb.getProgress() - 13;
                String message = txtIn.getText().toString();
                String output = encode(message, key);
                txtOut.setText(output);
                txtKey.setText("" + key);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


         */
        binding.fab.setOnClickListener(view -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Secret Message" +
                    DateFormat.getDateInstance().format(new Date()));
            shareIntent.putExtra(Intent.EXTRA_TEXT, txtOut.getText().toString());
            try {
                startActivity(Intent.createChooser(shareIntent, "Share message..."));
                finish();
            }
            catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "Error: Couldn't share.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

     */
}
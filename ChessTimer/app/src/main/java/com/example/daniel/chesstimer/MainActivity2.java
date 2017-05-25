package com.example.daniel.chesstimer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import static android.graphics.Typeface.createFromAsset;


public class MainActivity2 extends ActionBarActivity {

    //flag variable which tracks who's move it is.
    boolean currentlyWhitesMove = true;

    int totalMoves;

    //WHITE INITIALIZATION
    int whiteMoves;
    Long StartTime;
    CountDownTimer CountDownWhite;
    Long whitesPausedMilis = 0l;

    //BLACK INITIALIZATION
    int blackMoves;
    CountDownTimer CountDownBlack;
    Long blacksPausedMilis = 0l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);

        //formatting digital font of the numeric textviews
        Typeface tf = createFromAsset(getAssets(), "fonts/digital-7.ttf");
        TextView TV7 = (TextView)findViewById(R.id.textView7);
        TV7.setTypeface(tf);
        TextView TV8 = (TextView)findViewById(R.id.textView8);
        TV8.setTypeface(tf);
        TextView TV9 = (TextView)findViewById(R.id.textView9);
        TV9.setTypeface(tf);
        TextView TV10 = (TextView)findViewById(R.id.textView10);
        TV10.setTypeface(tf);

        /*
        -Recieves the time from the previous activity as strings.
        -Saves string variable into a more meaningful varible name for future use.
         */
        Intent intention2 = getIntent();
        String timeValReceived = intention2.getStringExtra("timeVal");
        String movesValReceived = intention2.getStringExtra("moveNum");

        //assign starting values to white and black move variables
        totalMoves = Integer.parseInt(movesValReceived);
        whiteMoves = totalMoves;
        blackMoves = totalMoves;

        //initializes the start time for white
        StartTime  = (long) TimeUnit.MINUTES.toMillis(Integer.parseInt(timeValReceived));
        blacksPausedMilis = StartTime;
        //creates the timer object for white and black
        CountDownWhite  = createTimer(StartTime, "White");
        movesRemaining("White");
        CountDownBlack  = createTimer(StartTime, "Black");
        movesRemaining("Black");

        //The timer begins when the second activity (this one here) srtars
        CountDownWhite.start();

        //button click logic
       final Button but = (Button)findViewById(R.id.buttonTurn);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            //toggles boolean which reflects who's turn it should be
            public void onClick(View v) {

                /*
                "If white's timer is currently counting down..
                 */
                if(currentlyWhitesMove ==  true){
                    /*
                    set the "flag" variable to false; this indicates that the
                    next time the button is pushed, the flag will switch back to "white"
                    */
                    currentlyWhitesMove = false;
                    /*
                    Stops and destroys the timer.
                     */
                    CountDownWhite.cancel();
                    //decreases the number of white's remaining moves
                    whiteMoves--;
                    /*
                    * Counts how many moves are left to be played, and displays that number
                    * in the corresponding text view.
                     */
                    movesRemaining("Black");
                    /*
                    If the user encounters the limit of moves, that was chosen earlier in this app,
                    then the timer resets, along with the move number.
                     */
                    allMovesCompleted(blackMoves, "Black");

                    but.setBackgroundColor(Color.BLACK);
                    but.setTextColor(Color.WHITE);
                    but.setText("Black's turn in progress");
                }else{
                    currentlyWhitesMove = true;
                    CountDownBlack.cancel();
                    blackMoves--;
                    movesRemaining("White");
                    allMovesCompleted(whiteMoves,"White");

                    but.setBackgroundColor(Color.WHITE);
                    but.setTextColor(Color.BLACK);
                    but.setText("White's turn in progress");
                }
            }
        });
    }

    /*
    - creates timer
    - outputs time to textview
    - saves current millis remaining
     */
    public CountDownTimer createTimer(long mls, final String player){

        //creates the timer object which serves as the output of this function
        CountDownTimer CDT = new CountDownTimer(mls, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                //At little confusing at first..
                String MinsSecs = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                );

                //saves the time remaining
                if(player =="White"){
                    whitesPausedMilis = millisUntilFinished;
                    TextView TV = (TextView) findViewById(R.id.textView7);
                    TV.setText(MinsSecs);
                }else if(player == "Black"){
                    blacksPausedMilis = millisUntilFinished;
                    TextView TV = (TextView) findViewById(R.id.textView9);
                    TV.setText(MinsSecs);
                }
            }

            @Override
            public void onFinish() {
                /*The string input 'player' for this method is also used here.
                * Checks if player is out of time
                * */
                showGameOver(player);
            }
        };

        return CDT;
    }

    public void movesRemaining(String side){

        if(side == "White"){
            TextView TV = (TextView) findViewById(R.id.textView8);

            if (whiteMoves == 0) {
                TV.setText(Integer.toString(10));
            }else{
                TV.setText(Integer.toString(whiteMoves));
            }
        } else if (side == "Black"){
            TextView TV = (TextView) findViewById(R.id.textView10);

            if(blackMoves == 0){
                TV.setText(Integer.toString(10));
            }else{
                TV.setText(Integer.toString(blackMoves));
            }
        }
    }

    /*
    * Determines what the starting value of the timer will be
    * for both white and black
    **/
    private void allMovesCompleted(int moves, String side){

        /*
        The input is the number of remaining moves on whatever timer object is currently counting down.
         */
        boolean completed = moves == 0 ? true : false;

        //if the last move was made, reset the timer
        if(completed == true && side == "White")
        {
            whiteMoves  = totalMoves;
            CountDownWhite.cancel();
            CountDownWhite  = createTimer(StartTime, "White");
            CountDownWhite.start();
        }
        else if(completed ==  false && side == "White")
        {
            CountDownWhite  = createTimer(whitesPausedMilis, "White");
            CountDownWhite.start();
        }
        else if(completed == true && side == "Black")
        {
            blackMoves  = totalMoves;
            CountDownBlack.cancel();
            CountDownBlack = createTimer(StartTime, "Black");
            CountDownBlack.start();
        }
        else if (completed == false && side == "Black")
        {
            CountDownBlack  = createTimer(blacksPausedMilis, "Black");
            CountDownBlack.start();
        }
    }

    private void showGameOver(String player){

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle("Game over!")
                .setMessage(player + " is out of time")
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //closes the app
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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
}

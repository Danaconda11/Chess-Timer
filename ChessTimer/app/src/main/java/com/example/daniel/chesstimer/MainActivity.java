package com.example.daniel.chesstimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.zip.Inflater;


public class MainActivity extends ActionBarActivity {

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView backgroundImage = (ImageView) findViewById(R.id.titleImage);

        /*
        * This was my attempt at programmatically resizing an image that was taken with my 16mp camera on my mobile phone.
        * */

//        backgroundImage.setImageBitmap(
//                decodeSampledBitmapFromResource(getResources(),
//                        R.drawable.piecestitlebackground,
//                        600,
//                        600
//                )
//        );

        TextView titleTap = (TextView) findViewById(R.id.title_tap);

        /*
        * So the start label appears on top of the imageview, which holds the background image
        * */
        titleTap.bringToFront();

        //when the title text is tapped on, in the first screen
        titleTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //a dialog box will appear so the user can set the amount of moves per time constraint (x moves in y time)

                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                // Get the layout inflater
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                //allows me to reference stuff within the dialog window; this took me forever to place correctly
                final View dialogLayout = inflater.inflate(R.layout.d_box_picker, null);

                //setup the values that can be used in the picker
                String[] nums = new String[10];
                int k = 1;
                for(int i=0; i<nums.length; i++, k++) {
                    nums[i] = Integer.toString(k * 10);
                }

                /**
                 * The Time number picker logic
                 **/
                final NumberPicker TimeObj = (NumberPicker) dialogLayout.findViewById(R.id.numberPickerTime);
                    TimeObj.setMaxValue(nums.length);
                    TimeObj.setMinValue(1);
                    TimeObj.setDisplayedValues(nums);
                    TimeObj.setFormatter(new NumberPicker.Formatter() {
                        @Override
                        public String format(int i) {
                            return String.format("%02d", i);
                        }
                    });

                final NumberPicker MovesObj = (NumberPicker) dialogLayout.findViewById(R.id.numberPickerMoves);
                    MovesObj.setMaxValue(nums.length);
                    MovesObj.setMinValue(1);
                    MovesObj.setDisplayedValues(nums);
                    MovesObj.setFormatter(new NumberPicker.Formatter() {
                        @Override
                        public String format(int i) {
                            return String.format("%02d", i);
                        }
                    });

                // 2. Chain together various setter methods to set the dialog characteristics
                 builder.setTitle("Set the timer details")
                        .setView(dialogLayout)
                        .setPositiveButton("Start", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //user selected OK
                                //save the values into the intent and start the next activity
                                Intent intention = new Intent(MainActivity.this, MainActivity2.class);
                                intention.putExtra("timeVal",Integer.toString(TimeObj.getValue() * 10));
                                intention.putExtra("moveNum",Integer.toString(MovesObj.getValue() * 10));
                                intention.putExtra("moveNum",Integer.toString(MovesObj.getValue() * 10));
                                startActivity(intention);
                            }
                        })
                        .setNegativeButton("Cancel", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //user selected CANCEL
                            }
                        });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
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
}

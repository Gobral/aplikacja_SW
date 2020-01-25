package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

public class AdapterPodgladuNagrania extends RecyclerView.Adapter< AdapterPodgladuNagrania.MyViewHolderNagranie>{

    public List<GlosoweEntity> lista_nagran;
    public Context context;
    private NotatkaEntity notatkaEntity;
    public RecyclerView rv;
    private final View.OnClickListener mOnClickListener = new AdapterPodgladuNagrania.MyOnClickListener();

    public static class MyViewHolderNagranie extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView_nazwa;
        public TextView textView_dlugosc;

        public MyViewHolderNagranie(View v) {
            super(v);
            textView_nazwa = v.findViewById(R.id.podglad_nagrania_nazwa);
            textView_dlugosc = v.findViewById(R.id.podglad_nagrania_dlugosc);
        }
    }

    public AdapterPodgladuNagrania(List lista_nagran, RecyclerView rv, Context context, NotatkaEntity notatkaEntity) {
        this.lista_nagran = lista_nagran;
        this.context = context;
        this.rv = rv;
        this.notatkaEntity = notatkaEntity;
    }

    public void setData(List<GlosoweEntity> newData) {
        this.lista_nagran= newData;
        notifyDataSetChanged();
    }

    @Override
    public AdapterPodgladuNagrania.MyViewHolderNagranie onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nagranie_glosowe_podglad, parent, false);
        v.setOnClickListener(mOnClickListener);
        AdapterPodgladuNagrania.MyViewHolderNagranie vh = new AdapterPodgladuNagrania.MyViewHolderNagranie(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterPodgladuNagrania.MyViewHolderNagranie holder, int position) {
        holder.textView_nazwa.setText(lista_nagran.get(position).getNazwaNagrania());
        MediaPlayer mediaPlayer = new MediaPlayer();
        int mFileDuration = 0;
        try {
            mediaPlayer.setDataSource(lista_nagran.get(position).getPath());
            mediaPlayer.prepare();
            mFileDuration = mediaPlayer.getDuration();
        } catch (IOException e) {
            System.out.println(e);
        }

        int sekundy =  mFileDuration / 1000;
        int minuty = sekundy / 60;
        int sekunda_minuty = sekundy - minuty * 60;
        holder.textView_dlugosc.setText("Długość nagrania: " + minuty + ":" + (sekunda_minuty < 10 ? "0" + sekunda_minuty : sekunda_minuty));
        mediaPlayer.release();
    }

    @Override
    public int getItemCount() {
        return lista_nagran.size();
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                int itemPosition = rv.getChildLayoutPosition(view);
                GlosoweEntity item = lista_nagran.get(itemPosition);
                openPlayer(item);
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
        public void openPlayer(GlosoweEntity we){

            MediaPlayer mPlayer = new MediaPlayer();
            Activity activity = (Activity) context;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            int mFileDuration = 0;
            Handler mHandler = new Handler();

            try {
                mPlayer.setDataSource(we.getPath());
                mPlayer.prepare();
                mFileDuration = mPlayer.getDuration();

            } catch (IOException e) {
                System.out.println(e);
            }

            int sekundy =  mFileDuration / 1000;
            int minuty = sekundy / 60;
            int sekunda_minuty = sekundy - minuty * 60;

            LayoutInflater li = LayoutInflater.from(activity);
            RelativeLayout relativeLayout = (RelativeLayout) li.inflate(R.layout.odtwarzacz_layout, null);

            TextView nazwaPliku = relativeLayout.findViewById(R.id.dialog_odtwrzacz_tytul);
            TextView dlugoscView = relativeLayout.findViewById(R.id.dialog_odtwrzacz_koniec);
            SeekBar seekBar = relativeLayout.findViewById(R.id.dialog_odtwrzacz_seekbar);
            ImageButton imageButton = relativeLayout.findViewById(R.id.dialog_odtwrzacz_przycisk);
            dlugoscView.setText(minuty + ":" + (sekunda_minuty < 10 ? "0" + sekunda_minuty : sekunda_minuty));

            MediaPlayer.OnCompletionListener cListener = new MediaPlayer.OnCompletionListener(){

                public void onCompletion(MediaPlayer mp){
                    imageButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
            };

            mPlayer.setOnCompletionListener(cListener);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mPlayer.isPlaying()){
                        mPlayer.start();
                        imageButton.setImageResource(R.drawable.ic_pause_black_24dp);
                    }
                    else {
                        mPlayer.pause();
                        imageButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    }
                }
            });

            seekBar.setMax(mFileDuration/1000);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(mPlayer != null && fromUser){
                        mPlayer.seekTo(progress * 1000);
                    }
                }
            });

            nazwaPliku.setText(we.getNazwaNagrania());

            builder.setView(relativeLayout);
            builder.setOnDismissListener((DialogInterface.OnDismissListener) dialog -> mPlayer.stop());
            AlertDialog alertDialog = builder.create();

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(mPlayer != null){
                        int mCurrentPosition = mPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    mHandler.postDelayed(this, 1000);
                }
            });

            alertDialog.show();
            //Intent intent = new Intent(context, EdycjaWpisuActivity.class);
            //intent.putExtra("nazwaWpisu", we.getNazwaWpisu());
            //intent.putExtra("nazwaNotatki", we.getNotatkaId());
            //context.startActivity(intent);
        }
    }
}

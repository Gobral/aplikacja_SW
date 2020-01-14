package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

public final class DodanieAsyncTask  extends AsyncTask<Void, Void, Integer> {

    private WeakReference<Activity> weakActivity;
    private Context context;

    public DodanieAsyncTask(Activity activity) {
        weakActivity = new WeakReference<>(activity);
        this.context = activity.getApplicationContext();
    }

    @Override
    protected Integer doInBackground(Void... params) {

        NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
        try {
            notatkiDb.notatkiDAO().insertNatatka(new NotatkaEntity("test4", new Date(2020, 2,2), "lorem psum"));
            notatkiDb.notatkiDAO().insertNatatka(new NotatkaEntity("test5", new Date(2020, 2,2), "lorem psum"));
            notatkiDb.notatkiDAO().insertNatatka(new NotatkaEntity("test6", new Date(2020, 2,2), "lorem psum"));
            notatkiDb.notatkiDAO().insertNatatka(new NotatkaEntity("test7", new Date(2020, 2,2), "lorem psum"));
            notatkiDb.notatkiDAO().insertNatatka(new NotatkaEntity("test8", new Date(2020, 2,2), "lorem psum"));
            notatkiDb.notatkiDAO().insertNatatka(new NotatkaEntity("test9", new Date(2020, 2,2), "lorem psum"));
            notatkiDb.notatkiDAO().insertNatatka(new NotatkaEntity("test10", new Date(2020, 2,2), "lorem psum"));
            notatkiDb.notatkiDAO().insertNatatka(new NotatkaEntity("test11", new Date(2020, 2,2), "lorem psum"));
        }
        catch (Exception e){

        }


        List<NotatkaEntity> wszystkie = notatkiDb.notatkiDAO().loadAllNotatki();
        for(NotatkaEntity w: wszystkie){
            System.out.println(w.getNazwaNotatki() + " " + w.getDataDodania() + " " + w.getZawartosc());
        }
        return wszystkie.size();
    }

    @Override
    protected void onPostExecute(Integer agentsCount) {
        Activity activity = weakActivity.get();
        if(activity == null) {
            return;
        }
        System.out.println(agentsCount);
        Toast.makeText(activity, "Tyle jest" + agentsCount, Toast.LENGTH_LONG).show();
    }
}

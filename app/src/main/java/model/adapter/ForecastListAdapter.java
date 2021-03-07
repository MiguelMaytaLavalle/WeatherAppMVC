package model.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherappmvc.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Parameter;

public class ForecastListAdapter extends RecyclerView.Adapter<ForecastListAdapter.ViewHolder> {
    private List<Parameter> mParameterList;
    private LayoutInflater mInflater;
    private Context mContext;

    public ForecastListAdapter(Context context, List<Parameter> parameterList) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mParameterList = parameterList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textValidTime, textTemperature, textCloudCover;
        private ImageView imageWeather;
        public ForecastListAdapter mAdapter;

        public ViewHolder(View itemView, ForecastListAdapter adapter) {
            //public ViewHolder(View itemView){
            super(itemView);
            textValidTime = itemView.findViewById(R.id.main_validtime);
            textTemperature = itemView.findViewById(R.id.main_temperature);
            textCloudCover = itemView.findViewById(R.id.main_cloudcover);
            imageWeather = itemView.findViewById(R.id.main_weathersituation);
            this.mAdapter = adapter;
        }

    }

    /**
     * Similiar to onCreate(). it inflates the item layout,
     * and returns a ViewHolder with the layout and the adapter.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.forecast_item, parent, false);
        return new ViewHolder(mItemView, this);
    }

    /**
     * Connects your data to the view holder.
     *
     * @param holder
     * @param position
     */
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Parameter parameter = mParameterList.get(position);
        holder.textValidTime.setText(parameter.getValidTime());
        holder.textTemperature.setText(parameter.getT() + "°C");
        holder.textCloudCover.setText("Cloud cover: " + parameter.getTcc_mean());
        holder.imageWeather.setImageResource(R.drawable.day_1);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = null;
        try {
            cal = Calendar.getInstance();
            Date date = formatter.parse(parameter.getValidTime());
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Daytime is between 06:00 and 18:00, else it is nighttime
        // Used with a switch to find correct image with Wsymb2 value.
        if(6 <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) < 18){
            switch (parameter.getWsymb2()) {
                case 1:
                    holder.imageWeather.setImageResource(R.drawable.day_1);
                    break;
                case 2:
                    holder.imageWeather.setImageResource(R.drawable.day_2);
                    break;
                case 3:
                    holder.imageWeather.setImageResource(R.drawable.day_3);
                    break;
                case 4:
                    holder.imageWeather.setImageResource(R.drawable.day_4);
                    break;
                case 5:
                    holder.imageWeather.setImageResource(R.drawable.day_5);
                    break;
                case 6:
                    holder.imageWeather.setImageResource(R.drawable.day_6);
                    break;
                case 7:
                    holder.imageWeather.setImageResource(R.drawable.day_7);
                    break;
                case 8:
                    holder.imageWeather.setImageResource(R.drawable.day_8);
                    break;
                case 9:
                    holder.imageWeather.setImageResource(R.drawable.day_9);
                    break;
                case 10:
                    holder.imageWeather.setImageResource(R.drawable.day_10);
                    break;
                case 11:
                    holder.imageWeather.setImageResource(R.drawable.day_11);
                    break;
                case 12:
                    holder.imageWeather.setImageResource(R.drawable.day_12);
                    break;
                case 13:
                    holder.imageWeather.setImageResource(R.drawable.day_13);
                    break;
                case 14:
                    holder.imageWeather.setImageResource(R.drawable.day_14);
                    break;
                case 15:
                    holder.imageWeather.setImageResource(R.drawable.day_15);
                    break;
                case 16:
                    holder.imageWeather.setImageResource(R.drawable.day_16);
                    break;
                case 17:
                    holder.imageWeather.setImageResource(R.drawable.day_17);
                    break;
                case 18:
                    holder.imageWeather.setImageResource(R.drawable.day_18);
                    break;
                case 19:
                    holder.imageWeather.setImageResource(R.drawable.day_19);
                    break;
                case 20:
                    holder.imageWeather.setImageResource(R.drawable.day_20);
                    break;
                case 21:
                    holder.imageWeather.setImageResource(R.drawable.day_21);
                    break;
                case 22:
                    holder.imageWeather.setImageResource(R.drawable.day_22);
                    break;
                case 23:
                    holder.imageWeather.setImageResource(R.drawable.day_23);
                    break;
                case 24:
                    holder.imageWeather.setImageResource(R.drawable.day_24);
                    break;
                case 25:
                    holder.imageWeather.setImageResource(R.drawable.day_25);
                    break;
                case 26:
                    holder.imageWeather.setImageResource(R.drawable.day_26);
                    break;
                case 27:
                    holder.imageWeather.setImageResource(R.drawable.day_27);
                    break;
            }
        }else{
            switch (parameter.getWsymb2()) {
                case 1:
                    holder.imageWeather.setImageResource(R.drawable.night_1);
                    break;
                case 2:
                    holder.imageWeather.setImageResource(R.drawable.night_2);
                    break;
                case 3:
                    holder.imageWeather.setImageResource(R.drawable.night_3);
                    break;
                case 4:
                    holder.imageWeather.setImageResource(R.drawable.night_4);
                    break;
                case 5:
                    holder.imageWeather.setImageResource(R.drawable.night_5);
                    break;
                case 6:
                    holder.imageWeather.setImageResource(R.drawable.night_6);
                    break;
                case 7:
                    holder.imageWeather.setImageResource(R.drawable.night_7);
                    break;
                case 8:
                    holder.imageWeather.setImageResource(R.drawable.night_8);
                    break;
                case 9:
                    holder.imageWeather.setImageResource(R.drawable.night_9);
                    break;
                case 10:
                    holder.imageWeather.setImageResource(R.drawable.night_10);
                    break;
                case 11:
                    holder.imageWeather.setImageResource(R.drawable.night_11);
                    break;
                case 12:
                    holder.imageWeather.setImageResource(R.drawable.night_12);
                    break;
                case 13:
                    holder.imageWeather.setImageResource(R.drawable.night_13);
                    break;
                case 14:
                    holder.imageWeather.setImageResource(R.drawable.night_14);
                    break;
                case 15:
                    holder.imageWeather.setImageResource(R.drawable.night_15);
                    break;
                case 16:
                    holder.imageWeather.setImageResource(R.drawable.night_16);
                    break;
                case 17:
                    holder.imageWeather.setImageResource(R.drawable.night_17);
                    break;
                case 18:
                    holder.imageWeather.setImageResource(R.drawable.night_18);
                    break;
                case 19:
                    holder.imageWeather.setImageResource(R.drawable.night_19);
                    break;
                case 20:
                    holder.imageWeather.setImageResource(R.drawable.night_20);
                    break;
                case 21:
                    holder.imageWeather.setImageResource(R.drawable.night_21);
                    break;
                case 22:
                    holder.imageWeather.setImageResource(R.drawable.night_22);
                    break;
                case 23:
                    holder.imageWeather.setImageResource(R.drawable.night_23);
                    break;
                case 24:
                    holder.imageWeather.setImageResource(R.drawable.night_24);
                    break;
                case 25:
                    holder.imageWeather.setImageResource(R.drawable.night_25);
                    break;
                case 26:
                    holder.imageWeather.setImageResource(R.drawable.night_26);
                    break;
                case 27:
                    holder.imageWeather.setImageResource(R.drawable.night_27);
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        if(mParameterList != null){
            return mParameterList.size();
        }else{
            return 0;
        }
    }

}
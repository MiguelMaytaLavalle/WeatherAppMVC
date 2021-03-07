package view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherappmvc.R;

import controller.MVCController;
import model.Forecast;
import model.MVCModel;
import model.adapter.ForecastListAdapter;
import com.example.weatherappmvc.RequestQueueManager;

/**
 * This is the View for displaying the UI.
 */
public class MainActivityViewImplementor implements MVCMainActivityView {
            View rootView;
            MVCController mvcController;

    private MVCModel mvcModel;
    private RequestQueueManager requestQueueManager;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private ForecastListAdapter mAdapter;
    private EditText mLatitudeEditText, mLongitudeEditText;
    private TextView mApprovedTimeTextView, mLatitudeTextView, mLongitudeTextView;
    private Button submitButton;
    private Context context;

    public MainActivityViewImplementor (Context context, ViewGroup container){
        rootView = LayoutInflater.from(context).inflate(R.layout.activity_main,container);
        this.context = context;
        this.mvcModel = new MVCModel();
        this.requestQueueManager = new RequestQueueManager(context);
        this.mvcController = new MVCController(mvcModel,this, requestQueueManager);
    }

    /**
     * Initializes the UI components
     * Also sets an onClickListener for when the Submit button
     * is clicked on.
     */
    @Override
    public void initComponents() {
        mLongitudeEditText = rootView.findViewById(R.id.edittext_longitude);
        mLatitudeEditText = rootView.findViewById(R.id.edittext_latitude);
        mApprovedTimeTextView = rootView.findViewById(R.id.textview_header_approvedtime);
        mLatitudeTextView = rootView.findViewById(R.id.textview_header_latitude);
        mLongitudeTextView = rootView.findViewById(R.id.textview_header_longitude);

        mRecyclerView = rootView.findViewById(R.id.recyclerview);
        mAdapter = new ForecastListAdapter(context, mvcController.getForecastList());
        dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                new LinearLayoutManager(context).getOrientation());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mAdapter);

        submitButton = rootView.findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener(){

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                float latitude,longitude;
                try{
                    latitude = Float.parseFloat(mLatitudeEditText.getText().toString());
                    longitude = Float.parseFloat(mLongitudeEditText.getText().toString());
                    mvcController.onSubmitButtonClicked(latitude, longitude);
                }catch (NumberFormatException e){
                    messageToast("Please Enter Coordinates");
                }
            }
        });
    }

    @Override
    public RecyclerView.Adapter getForecastListAdapter() {
        return mAdapter;
    }

    /**
     * Invoked for updating the UI with wither new or old data.
     */
    @Override
    public void bindDataToView() {
        mvcController.onViewLoaded();
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    public void messageToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    public TextView getApprovedTimeTextView(){return mApprovedTimeTextView;}

    public TextView getLatitudeTextView (){return mLatitudeTextView;}

    public TextView getLongitudeTextView(){return mLongitudeTextView;}

    //TODO Maybe fetch GPS coordinates when retrieving fresh data
    // after an hour has passed? Right now just fetch coordinates
    // for KTH Flemingsberg Campus...
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getFreshData(){
        float latitude = (float) 59.2188, longitude = (float) 17.9443;
        mvcController.onSubmitButtonClicked(latitude, longitude);
    }
    /**
     * This will update the values in the UI, set them to VISIBLE
     * and update the adapter.
     * @param forecast
     */
    @SuppressLint("SetTextI18n")
    @Override
            public void showForecastView(Forecast forecast) {
        mApprovedTimeTextView.setText("Approved Time: " + forecast.getApprovedTime());
        mLatitudeTextView.setText("Latitude: " + forecast.getCoordinates().getLatitude());
        mLongitudeTextView.setText("Longitude: " + forecast.getCoordinates().getLongitude());
        setViewVisible();
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void setViewInvisible(){
        mLongitudeTextView.setVisibility(rootView.INVISIBLE);
        mLatitudeTextView.setVisibility(rootView.INVISIBLE);
        mApprovedTimeTextView.setVisibility(rootView.INVISIBLE);
        mRecyclerView.setVisibility(rootView.INVISIBLE);
    }
    @Override
    public void setViewVisible(){
        mLongitudeTextView.setVisibility(rootView.VISIBLE);
        mLatitudeTextView.setVisibility(rootView.VISIBLE);
        mApprovedTimeTextView.setVisibility(rootView.VISIBLE);
        mRecyclerView.setVisibility(rootView.VISIBLE);
    }
}

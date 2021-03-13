package com.example.dotgeneration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotgeneration.utils.DateTimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistoryPage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);
        ImageView backImg = findViewById(R.id.activity_history_page_back_img);
        RecyclerView resultView = findViewById(R.id.activity_history_page_recycle_view_records);
        final Bundle bundle = getIntent().getExtras();
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryPage.this, MainMenuPage.class);
                intent.putExtra("nric", bundle.getString("nric"));
                startActivity(intent);
            }
        });
        try {
            JSONArray records = new JSONArray(bundle.getString("records"));
            DividerItemDecoration decorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            decorator.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.record_divider_shape));
            resultView.setAdapter(new CustomAdapter(records));
            resultView.setLayoutManager(new LinearLayoutManager(this));
            resultView.addItemDecoration(decorator);
            resultView.setItemAnimator(new DefaultItemAnimator());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        JSONArray records;
        private TextView record_item_date_time, record_item_total_timeTaken;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public ViewHolder(View view) {
                super(view);
                record_item_date_time = view.findViewById(R.id.record_item_date_time_txt);
                record_item_total_timeTaken = view.findViewById(R.id.record_item_total_time_txt);
                view.setOnClickListener(this);
            }
            TextView getRecordItemDateTime(){
                return record_item_date_time;
            }

            TextView getTotalTimeTaken(){ return record_item_total_timeTaken; }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryPage.this,  DetailPage.class);
                intent.putExtra("records", records + "");
                try {
                    intent.putExtra("record", records.get(this.getLayoutPosition()) + "");
                    intent.putExtra("nric", getIntent().getStringExtra("nric"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        }

        public CustomAdapter(JSONArray records) {
             this.records = records;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.record_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            try {
                JSONObject record = records.getJSONObject(position);
                int totalTime = record.getInt("scoreA") + record.getInt("scoreB");
                viewHolder.getRecordItemDateTime().setText(DateTimeUtils.convert(record.getString("createdAt")));
                viewHolder.getTotalTimeTaken().setText("Total time taken: " + totalTime + " second");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return records.length();
        }

    }

}
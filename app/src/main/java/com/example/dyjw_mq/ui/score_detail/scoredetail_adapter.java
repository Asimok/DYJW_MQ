package com.example.dyjw_mq.ui.score_detail;



        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.res.Resources;
        import android.graphics.drawable.Drawable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.LinearLayout;
        import android.widget.TextView;


        import com.example.dyjw_mq.R;
        import com.example.dyjw_mq.ui.gpa.gpa_datainfo;
        import com.github.mikephil.charting.charts.BarChart;
        import com.github.mikephil.charting.components.XAxis;
        import com.github.mikephil.charting.data.BarData;
        import com.github.mikephil.charting.data.BarDataSet;
        import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
        import com.github.mikephil.charting.utils.ColorTemplate;

        import java.util.ArrayList;


public class scoredetail_adapter extends BaseAdapter {
    public int count;
    protected Context context;
    protected LayoutInflater inflater;
    protected int resource;
    protected ArrayList<scoredetail_datainfo> data;

    public scoredetail_adapter(Context context,  ArrayList<scoredetail_datainfo> data){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.resource = resource;
        this.count = count;
        if(data==null){
            this.data=new ArrayList<>();

        }else{
            this.data = data;

        }
    }


    @Override
    public int getCount() {
        return data.size();

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.showscoreanalyze, null);


        TextView xuenian = view.findViewById(R.id.xuenian);
        TextView max = view.findViewById(R.id.maxscore);
        TextView mean = view.findViewById(R.id.mean);
        TextView gpa = view.findViewById(R.id.gpa);
        TextView passrate = view.findViewById(R.id.passrate);
         BarChart chart=view.findViewById(R.id.jd_chart);
        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(true);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        chart.getAxisLeft().setDrawGridLines(false);
        // add a nice and smooth animation
        chart.animateY(1500);

        chart.getLegend().setEnabled(false);

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(data.get(position).getValues());
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(data.get(position).getValues(), "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            chart.setData(data);
            chart.setFitBars(true);
        }

        chart.invalidate();






        xuenian.setText(data.get(position).getXuenian());
        max.setText(data.get(position).getMaxscore());
        mean.setText( data.get(position).getMean());
        gpa.setText( data.get(position).getGpa());
        passrate.setText(data.get(position).getPassrate());


        return view;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }
}

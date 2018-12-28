package mission_control.felixseip.com.missioncontrol.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mission_control.felixseip.com.missioncontrol.R;
import mission_control.felixseip.com.missioncontrol.Rocket;

public class RocketAdapter extends ArrayAdapter<Rocket> {

    public RocketAdapter(Context context, ArrayList<Rocket> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Rocket rocket = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rocket_list_item, parent, false);
        }

        // Lookup view for data population
        TextView rocketName = (TextView) convertView.findViewById(R.id.rocket_name);
        TextView rocketType = (TextView) convertView.findViewById(R.id.rocket_type);

        // Populate the data into the template view using the data object
        rocketName.setText(rocket.getName());
        rocketType.setText(rocket.getType());
        // Return the completed view to render on screen
        return convertView;
    }


}

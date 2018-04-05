package byobgyn.com.synapse.elements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import byobgyn.com.synapse.R;
import byobgyn.com.synapse.entities.Node;
import byobgyn.com.synapse.utils.StringUtils;

public class NodeAdapter extends ArrayAdapter<Node> implements Filterable {
    private ArrayList<Node> nodes;
    private ArrayList<Node> filtered;
    private LayoutInflater inflator;

    public NodeAdapter(Context context, int textViewResourceId, ArrayList<Node> nodes) {
        super(context, textViewResourceId, nodes);
        this.nodes = nodes;
        this.filtered = nodes;
        inflator = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = inflator.inflate(R.layout.node_list_item, null);

            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtSecurity = (TextView) convertView.findViewById(R.id.txtSecurity);
            holder.txtDistance = (TextView) convertView.findViewById(R.id.txtDistance);
            holder.txtDuration = (TextView) convertView.findViewById(R.id.txtDuration);
            holder.txtEconomy = (TextView) convertView.findViewById(R.id.txtEconomy);
            holder.txtPopulation = (TextView) convertView.findViewById(R.id.txtPopulation);
            holder.txtGovernment = (TextView) convertView.findViewById(R.id.txtGovernment);
            holder.txtCost = (TextView) convertView.findViewById(R.id.txtCost);
            holder.txtState = (TextView) convertView.findViewById(R.id.txtState);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Node node = filtered.get(position);
        if(node != null) {
            holder.txtName.setText(node.getName());
            holder.txtSecurity.setText(node.getSecurity());
            holder.txtDuration.setText(node.getDuration());
            holder.txtDistance.setText(node.getDistance());
            holder.txtEconomy.setText(node.getEconomy());
            holder.txtPopulation.setText(node.getPopulation());
            holder.txtGovernment.setText(node.getGovernment());
            holder.txtCost.setText(StringUtils.convert(node.getCost()));
            holder.txtState.setText(node.getState());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtSecurity;
        TextView txtDistance;
        TextView txtDuration;
        TextView txtEconomy;
        TextView txtPopulation;
        TextView txtGovernment;
        TextView txtCost;
        TextView txtState;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filter = constraint.toString().toLowerCase();

                final ArrayList<Node> rvalues = new ArrayList<>();
                for(int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    if(node.getName().toLowerCase().startsWith(filter)) {
                        rvalues.add(node);
                    }
                }

                FilterResults results = new FilterResults();
                results.count = rvalues.size();
                results.values = rvalues;
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtered = (ArrayList<Node>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}

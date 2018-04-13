package byobgyn.com.synapse.elements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import byobgyn.com.synapse.R;
import byobgyn.com.synapse.entities.Node;
import byobgyn.com.synapse.utils.StringUtils;

public class NodeAdapter extends ArrayAdapter<Node> implements Filterable {
    private ArrayList<Node> nodes;
    private ArrayList<Node> filtered;
    private LayoutInflater inflater;

    public NodeAdapter(Context context, int textViewResourceId, ArrayList<Node> nodes) {
        super(context, textViewResourceId, nodes);
        this.nodes = nodes;
        this.filtered = nodes;
        inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.node_list_item, null);

            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtSecurity = (TextView) convertView.findViewById(R.id.txtSecurity);
            holder.txtDistance = (TextView) convertView.findViewById(R.id.txtDistance);
            holder.txtDuration = (TextView) convertView.findViewById(R.id.txtDuration);
            holder.txtCost = (TextView) convertView.findViewById(R.id.txtCost);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(filtered.size() > position) {
            Node node = filtered.get(position);
            if (node != null) {
                holder.txtName.setText(node.getName());
                holder.txtSecurity.setText(node.getSecurity());
                holder.txtDistance.setText(String.format("%s LY", node.getDistance()));
                holder.txtCost.setText(String.format("%s CR", StringUtils.convert(node.getCost())));
                holder.txtDuration.setText(String.format("%s MINS", node.getDuration()));
            }
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtSecurity;
        TextView txtDistance;
        TextView txtDuration;
        TextView txtCost;
    }

    @Override @NotNull
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

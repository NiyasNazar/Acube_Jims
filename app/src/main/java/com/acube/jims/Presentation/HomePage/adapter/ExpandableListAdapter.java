package com.acube.jims.Presentation.HomePage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.HomePage.NavMenuModel;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<NavMenuModel> listDataHeader;
        private HashMap<NavMenuModel, List<NavMenuModel>> listDataChild;

    public ExpandableListAdapter(Context context, List<NavMenuModel> listDataHeader,
                HashMap<NavMenuModel, List<NavMenuModel>> listChildData) {
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.listDataChild = listChildData;
        }

        @Override
        public NavMenuModel getChild(int groupPosition, int childPosititon) {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
        boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = getChild(groupPosition, childPosition).menuName;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_child, null);
            }

            TextView txtListChild = convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
                return 0;
            else
                return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                        .size();
        }

        @Override
        public NavMenuModel getGroup(int groupPosition) {
            return this.listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this.listDataHeader.size();

        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = getGroup(groupPosition).menuName;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_header, null);
            }

            TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
          //  lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
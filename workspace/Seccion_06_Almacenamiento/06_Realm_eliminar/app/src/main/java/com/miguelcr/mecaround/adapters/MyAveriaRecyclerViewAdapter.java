package com.miguelcr.mecaround.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miguelcr.mecaround.interfaces.OnAveriaInteractionListener;
import com.miguelcr.mecaround.R;
import com.miguelcr.mecaround.models.AveriaDB;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmResults;


public class MyAveriaRecyclerViewAdapter extends RecyclerView.Adapter<MyAveriaRecyclerViewAdapter.ViewHolder> {

    private final RealmResults<AveriaDB> mValues;
    private final OnAveriaInteractionListener mListener;
    private Context ctx;
    private RealmChangeListener listenerRefresco;

    public MyAveriaRecyclerViewAdapter(Context context, RealmResults<AveriaDB> items, OnAveriaInteractionListener listener) {
        ctx = context;
        mValues = items;
        mListener = listener;
        this.listenerRefresco = new RealmChangeListener<OrderedRealmCollection<AveriaDB>>() {
            @Override
            public void onChange(OrderedRealmCollection<AveriaDB> results) {
                notifyDataSetChanged();
            }
        };

        if (items != null) {
            addListener(items);
        }
    }

    private void addListener(OrderedRealmCollection<AveriaDB> items) {
        if (items instanceof RealmResults) {
            RealmResults realmResults = (RealmResults) items;
            realmResults.addChangeListener(listenerRefresco);
        } else if (items instanceof RealmList) {
            RealmList<AveriaDB> list = (RealmList<AveriaDB>) items;
            //noinspection unchecke
            list.addChangeListener((RealmChangeListener) listenerRefresco);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + items.getClass());
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_listado_averias_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.textViewTitulo.setText(holder.mItem.getTitulo());
        holder.textViewModeloCoche.setText(holder.mItem.getModeloCoche());
        holder.textViewNumPresupuestos.setText(holder.mItem.getNumeroPresupuestos()+" presupuestos");

        if(!holder.mItem.getUrlFoto().isEmpty()) {
            Glide.with(ctx)
                    .load(holder.mItem.getUrlFoto())
                    .into(holder.imageViewFotoAveria);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onAveriaClick(holder.mItem);
                }
            }
        });

        holder.imageViewEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onAveriaEdit(holder.mItem);
                }
            }
        });

        holder.imageViewEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onAveriaEliminar(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewTitulo;
        public final TextView textViewModeloCoche;
        public final TextView textViewNumPresupuestos;
        public final ImageView imageViewFotoAveria;
        public final ImageView imageViewEditar;
        public final ImageView imageViewEliminar;
        public AveriaDB mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewTitulo = (TextView) view.findViewById(R.id.textViewTitulo);
            textViewModeloCoche = (TextView) view.findViewById(R.id.textViewModeloCoche);
            textViewNumPresupuestos = (TextView) view.findViewById(R.id.textViewPresupuesto);
            imageViewFotoAveria = (ImageView) view.findViewById(R.id.imageViewFoto);
            imageViewEditar = (ImageView) view.findViewById(R.id.imageViewEditar);
            imageViewEliminar = (ImageView) view.findViewById(R.id.imageViewEliminarAveria);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewTitulo.getText() + "'";
        }
    }
}

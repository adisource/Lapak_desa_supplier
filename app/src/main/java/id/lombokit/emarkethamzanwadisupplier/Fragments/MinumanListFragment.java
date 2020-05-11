package id.lombokit.emarkethamzanwadisupplier.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.lombokit.emarkethamzanwadisupplier.DetailPageActivity;
import id.lombokit.emarkethamzanwadisupplier.Models.Categories;
import id.lombokit.emarkethamzanwadisupplier.Models.Products;
import id.lombokit.emarkethamzanwadisupplier.R;


public class MinumanListFragment extends Fragment {

    private LinearLayout linear_progressbar;

    private Toolbar toolbar;
    private TextView toolBarTxt;

    private RecyclerView recyclerView;
    private RecycleAdapter_AddProduct mAdapter;
    private int status_code;
    private String token, totalPriceOfProducts;


//    private ProductArrayList productsArrayList;

    private TextView quantityOfTotalProduct, priceOfTotalProduct, next;
    private Categories categories;

    private int[] IMAGES = {R.drawable.asel, R.drawable.aqua, R.drawable.narmada, R.drawable.galon};
    private String[] NamES = {"Asel", "Aqua", "Narmada", "Air Galon"};
    private String[] PRICE = {"24000", "42000", "30000", "5000"};


    private View view;


    Animation startAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_minuman, container, false);
        startAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        initComponent(view);

        categories = new Categories();
        categories.productsArrayList = new ArrayList<>();


        /*for (int i = 0; i < NamES.length; i++) {
            Products products = new Products();
            products.setName(NamES[i]);
            products.setPrice(PRICE[i]);
            products.setImage(IMAGES[i]);

            categories.productsArrayList.add(products);

        }*/


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

    /*    next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ProductArrayList productsForSend = new ProductArrayList();
                productsForSend =  productsToBeSend(categoriesAndProducts);
                PrefUtils.setProducts(productsForSend, getActivity());

                if (productsForSend.getProductsArrayList().size() >0){
                    Intent it = new Intent(getActivity(), ProductSummaryActivity.class);
                    startActivity(it);
                }else {
                    Toast.makeText(getActivity(), "Please Select The Products", Toast.LENGTH_SHORT).show();
                }


            }
        });*/


        mAdapter = new RecycleAdapter_AddProduct(getActivity(), categories);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), DetailPageActivity.class);
                startActivity(i);
            }
        });


        return view;

    }

    private void initComponent(View view) {


    }

    public class RecycleAdapter_AddProduct extends RecyclerView.Adapter<RecycleAdapter_AddProduct.MyViewHolder> {

        Context context;
        boolean showingFirst = true;
        private Categories categories;
        int recentPos = -1;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView title;
            TextView price;
            TextView quantityTxt;
            private LinearLayout llMinus, llPlus;
            int quantity;


            public MyViewHolder(View view) {
                super(view);
                image = (ImageView) view.findViewById(R.id.image);
                title = (TextView) view.findViewById(R.id.title);
                price = (TextView) view.findViewById(R.id.price);
               
            }
        }


        public RecycleAdapter_AddProduct(Context context, Categories categories) {
            this.categories = categories;
            this.context = context;
        }

        @Override
        public RecycleAdapter_AddProduct.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_product, parent, false);

            return new RecycleAdapter_AddProduct.MyViewHolder(itemView);
        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final RecycleAdapter_AddProduct.MyViewHolder holder, final int position) {
//            Products movie = productsList.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ind", "" + position);
                    Intent i = new Intent(getContext(), DetailPageActivity.class);
                    startActivity(i);
                }
            });

           /* holder.title.setText(categories.getProductsArrayList().get(position).getName());
            holder.price.setText(categories.getProductsArrayList().get(position).getPrice());
            holder.quantityTxt.setText(categories.getProductsArrayList().get(position).getQuantity() + "");
            holder.image.setImageDrawable(getResources().getDrawable(categories.getProductsArrayList().get(position).getImage()));

            holder.quantity = categories.getProductsArrayList().get(position).getQuantity();
            int totalPrice = holder.quantity * Integer.parseInt(categories.getProductsArrayList().get(position).getPrice());


            if (position == recentPos) {
                Log.e("pos", "" + recentPos);
                // start animation
                holder.quantityTxt.startAnimation(startAnimation);
            } else {
                holder.quantityTxt.clearAnimation();

            }

            if (categories.getProductsArrayList().get(position).getQuantity() > 0) {
                holder.quantityTxt.setVisibility(View.VISIBLE);
                holder.llMinus.setVisibility(View.VISIBLE);
            } else {
                holder.quantityTxt.setVisibility(View.GONE);
                holder.llMinus.setVisibility(View.GONE);
            }


            categories.getProductsArrayList().get(position).setPriceAsPerQuantity("" + totalPrice);


            holder.llPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.quantity < 10) {

                        recentPos = position;
                        holder.quantity = holder.quantity + 1;
                        categories.getProductsArrayList().get(position).setQuantity(holder.quantity);
                        categories.getProductsArrayList().get(position).setPriceAsPerQuantity("" + holder.quantity * Integer.parseInt(categories.getProductsArrayList().get(position).getPrice()));

                        holder.quantityTxt.setText("" + holder.quantity);
                    }
                    notifyDataSetChanged();

                }
            });


            holder.llMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.quantity > 0 && holder.quantity <= 10) {

                        recentPos = position;

                        holder.quantity = holder.quantity - 1;
                        categories.getProductsArrayList().get(position).setQuantity(holder.quantity);
                        categories.getProductsArrayList().get(position).setPriceAsPerQuantity("" + holder.quantity * Integer.parseInt(categories.getProductsArrayList().get(position).getPrice()));

                        holder.quantityTxt.setText("" + holder.quantity);


                    }

                    notifyDataSetChanged();

                }
            });*/

        }

        @Override
        public int getItemCount() {
            return categories.getProductsArrayList().size();
        }

    }

}


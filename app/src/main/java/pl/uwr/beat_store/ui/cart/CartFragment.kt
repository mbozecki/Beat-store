package pl.uwr.beat_store.ui.cart

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import pl.uwr.beat_store.R
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.utils.PaymentsUtil

class CartFragment : Fragment() {

    private lateinit var cartViewModel: CartViewModel
    private var layoutManager: RecyclerView.LayoutManager? = null;
    private var adapter: RecyclerView.Adapter<CartAdapter.CartViewHolder>? = null;
    private var songList= ArrayList<Song>();
    private var totalPrice=0.0;
    private lateinit var totalText : TextView;
    private lateinit var payButton: Button;
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991
    private lateinit var paymentsClient: PaymentsClient


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        var rvCart: RecyclerView = view.findViewById(R.id.rvCart);
        layoutManager= LinearLayoutManager(requireContext());
        adapter= CartAdapter(songList, requireContext());
        rvCart.layoutManager= layoutManager;
        rvCart.adapter= adapter;


        totalText= view.findViewById(R.id.total);
        payButton=  view.findViewById(R.id.pay_button);
        totalText.text = "Total: "+totalPrice.toString()+"$";
        payButton.setOnClickListener {
            requestPayment();
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        lifecycle.addObserver(cartViewModel);
        cartViewModel.getCartLiveData().observe(viewLifecycleOwner, {
            songList=it;
            for(x in it)
            {
                totalPrice+=x.price;
            }

            onViewCreated(requireView(), savedInstanceState)

        })
        paymentsClient = PaymentsUtil.createPaymentsClient(requireActivity())

        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            // Value passed in AutoResolveHelper
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK ->
                        data?.let { intent ->
                            //PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                        }

                    RESULT_CANCELED -> {
                        // The user cancelled the payment attempt
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            //handleError(it.statusCode)
                        }
                    }
                }


            }
        }
    }

    private fun requestPayment() {

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        val garmentPrice = 13021;
        val priceCents = totalPrice;

        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents)
        if (paymentDataRequestJson == null) {
            Log.e("RequestPayment", "Can't fetch payment data request")
            return
        }
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(request), requireActivity(), LOAD_PAYMENT_DATA_REQUEST_CODE)
        }
    }

}
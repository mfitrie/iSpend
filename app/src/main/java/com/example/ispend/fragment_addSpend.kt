package com.example.ispend

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_add_spend.*
import java.lang.ClassCastException
import java.lang.Exception
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_addSpend.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_addSpend : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_spend, container, false)
    }
















    interface OnInputListener{
        fun sendInput(value: Float, type: String, date: String)
    }

    var mInputListener: OnInputListener ? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // remove white background
        dialog?.window?.setBackgroundDrawableResource(R.drawable.popup_border)

        food_type()
        fuel_type()
        entertainment_type()
        other_type()
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_addSpend().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mInputListener = activity as OnInputListener
        }catch (e: Exception){
            Log.d("ON_ATTACH", "onAttach: ${e.message}")
        }
    }








    @RequiresApi(Build.VERSION_CODES.O)
    fun food_type(){
        val TAG = "food_type"
        btn_food.setOnClickListener(){
            val type = "Food"

            // Date
            val getDate = date()

            if(!et_spendValue.text.isEmpty()){
                val spendValue = formattedDecimalPlaces(et_spendValue)
                Log.d(TAG, "value: $spendValue, Date: $getDate")

                callInterfaceInput(spendValue, type, getDate)
                dismiss()

            }else{
                Toast.makeText(context, "Please enter your amount", Toast.LENGTH_SHORT).show()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun entertainment_type(){
        val TAG = "entertainment_type"
        btn_entertainment.setOnClickListener(){
            val type = "Entertainment"

            // Date
            val getDate = date()

            if(!et_spendValue.text.isEmpty()){
                val spendValue = formattedDecimalPlaces(et_spendValue)
                Log.d(TAG, "value: $spendValue, Date: $getDate")

                callInterfaceInput(spendValue, type, getDate)
                dismiss()

            }else{
                Toast.makeText(context, "Please enter your amount", Toast.LENGTH_SHORT).show()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fuel_type(){
        val TAG = "fuel_type"
        btn_fuel.setOnClickListener(){
            val type = "Fuel"

            // Date
            val getDate = date()

            if(!et_spendValue.text.isEmpty()){
                val spendValue = formattedDecimalPlaces(et_spendValue)
                Log.d(TAG, "value: $spendValue, Date: $getDate")

                callInterfaceInput(spendValue, type, getDate)
                dismiss()

            }else{
                Toast.makeText(context, "Please enter your amount", Toast.LENGTH_SHORT).show()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun other_type(){
        val TAG = "other_type"
        btn_other.setOnClickListener(){
            val type = "Other"

            // Date
            val getDate = date()

            if(!et_spendValue.text.isEmpty()){
                val spendValue = formattedDecimalPlaces(et_spendValue)
                Log.d(TAG, "value: $spendValue, Date: $getDate")

                callInterfaceInput(spendValue, type, getDate)
                dismiss()


            }else{
                Toast.makeText(context, "Please enter your amount", Toast.LENGTH_SHORT).show()
            }

        }
    }


    /////////////////////////////////// FUNCTION HELPER ///////////////////////////////////
    // to format date
    @RequiresApi(Build.VERSION_CODES.O)
    fun date(): String{
        val date = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        return date.format(formatter)
    }
    
    
    // to format decimal places
    fun formattedDecimalPlaces(number: EditText): Float{
        val decimal = DecimalFormat("#,###.##")
        val value = number.text.toString().toFloat()

        return decimal.format(value).toFloat()

    }


    // function to input and pass to MainActivity
    fun callInterfaceInput(value: Float, type: String, date: String){
        mInputListener?.sendInput(value, type, date)
    }


}
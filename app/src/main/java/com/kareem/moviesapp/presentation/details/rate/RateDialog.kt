package com.kareem.moviesapp.presentation.details.rate

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kareem.moviesapp.R
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.databinding.DialogRateOrderBinding

class RateDialog(val movie:Movie,private val onSuccess:(rating:Float)->Unit ): BottomSheetDialogFragment(){

    private var binding: DialogRateOrderBinding? = null

    companion object {
        const val TAG = "RateBottomSheetDialog"
    }


    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = View.inflate(context, R.layout.dialog_rate_order, null)
        dialog.setContentView(view)
        binding = DialogRateOrderBinding.bind(view)
        binding?.tvTitle?.text=movie.title
        binding?.ratingBar?.setOnRatingBarChangeListener { ratingBar, rate, b ->
            if (rate>0f){
                enableBtn()
            }
            else{
                disableBtn()
            }
        }
        binding?.btnRateOrder?.setOnClickListener {
            onSuccess.invoke(binding?.ratingBar?.rating!!)
        }

    }

    private fun enableBtn() {
        binding?.btnRateOrder?.isEnabled=true
        binding?.btnRateOrder?.alpha=1f
    }

    private fun disableBtn() {
        binding?.btnRateOrder?.isEnabled=false
        binding?.btnRateOrder?.alpha=0.5f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)

    }

}
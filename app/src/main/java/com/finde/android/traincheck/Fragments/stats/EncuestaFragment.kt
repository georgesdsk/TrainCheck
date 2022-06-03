package com.finde.android.traincheck.Fragments.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.FireBaseReferencies
import com.finde.android.traincheck.databinding.FragmentEncuestaBinding
import com.google.android.material.radiobutton.MaterialRadioButton
import java.text.SimpleDateFormat
import java.util.*


class EncuestaFragment : Fragment() {
    var listaPreguntas = listOf(
        "¿Cuanto te has esforzado hoy?",
        "¿Qué resultados has obtenido?",
        "¿Cuando estabas motivado?",
        "¿Cómo de cansado te has sentido?",
        "¿Cómo has dormido y comido?",
    )
    var listaRespuestas = listOf("mucho", "bastante", "suficiente", "poco", "muyPoco")
    var respondido = mutableListOf<String>()
    var contador: Int = 0
    private val format = SimpleDateFormat("dd-MM-yyyy")
    private lateinit var mBinding: FragmentEncuestaBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentEncuestaBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.pregunta.text = listaPreguntas[0]
        mBinding.button.setOnClickListener {
            if (mBinding.radioGroup.checkedRadioButtonId != -1) {
                val check = requireActivity().findViewById(mBinding.radioGroup.checkedRadioButtonId) as MaterialRadioButton?
                respondido.add(check!!.text.toString())
                nextQuestion()
            }
        }

    }

    private fun nextQuestion() {
        val navigation = Navigation.findNavController(mBinding.root)
        if (contador == listaPreguntas.size - 2) {
            mBinding.button.text = "Finalizar"
        }
        if (contador == listaPreguntas.size - 1) {
            FireBaseReferencies.mAtletasRef.child(FireBaseReferencies.mFirebaseAuth.currentUser!!.uid).child("listStats").child(format.format(
                Date()
            )).setValue(respondido)
            navigation.navigate(R.id.action_encuestaFragment_to_statsFragment)
            Toast.makeText(requireContext(), "Encuesta finalizada", Toast.LENGTH_SHORT).show()
        } else {
            mBinding.pregunta.text = listaPreguntas[++contador]
        }


    }


}
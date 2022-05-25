package com.finde.android.traincheck.Fragments.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finde.android.traincheck.databinding.ActivityMainBinding.inflate
import com.finde.android.traincheck.databinding.FragmentAddBinding
import com.finde.android.traincheck.databinding.FragmentEncuestaBinding

class EncuestaFragment : Fragment() {
    var listaPreguntas = listOf(
        "¿Cuanto te has esforzado hoy?",
        "¿Qué resultados has obtenido?",
        "¿Cuando estabas motivado?",
        "¿Cómo de cansado te has sentido?",
        "¿Cómo has dormido y comido?",
    )
    var listaRespuestas = listOf("mucho", "bastante", "suficiente", "poco", "muyPoco")
    var respondido = mutableListOf<Int>()
    var contador: Int = 0
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
                respondido.add(mBinding.radioGroup.checkedRadioButtonId)
                nextQuestion()
            }
        }

    }

    private fun nextQuestion() {
        mBinding.pregunta.text = listaPreguntas[++contador]
    }


}
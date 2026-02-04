package com.grupo5.tickets4u

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar

class CrearEventoDialogFragment : DialogFragment() {

    var onEventoCreado: (() -> Unit)? = null

    private var fechaInicioIso = ""
    private var fechaFinIso = ""

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_crear_evento, container, false)

        val spinner = view.findViewById<Spinner>(R.id.spinnerCategoria)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            arrayOf("ACTUAL", "DESTACADO", "INTERNACIONAL")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        view.findViewById<Button>(R.id.btnFechaInicio)
            .setOnClickListener { showDateTimePicker { fechaInicioIso = it } }

        view.findViewById<Button>(R.id.btnFechaFin)
            .setOnClickListener { showDateTimePicker { fechaFinIso = it } }

        view.findViewById<Button>(R.id.btnCrear).setOnClickListener {

            val nombre = view.findViewById<EditText>(R.id.etNombre).text.toString()
            val aforoStr = view.findViewById<EditText>(R.id.etAforo).text.toString()

            if (nombre.isBlank() || fechaInicioIso.isBlank() || aforoStr.isBlank()) {
                Toast.makeText(context, "Rellena los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val evento = Event(
                nombre = nombre,
                descripcion = view.findViewById<EditText>(R.id.etDescripcion).text.toString(),
                fechaInicio = fechaInicioIso,
                fechaFin = fechaFinIso,
                ciudad = view.findViewById<EditText>(R.id.etCiudad).text.toString(),
                ubicacion = view.findViewById<EditText>(R.id.etUbicacion).text.toString(),
                direccion = view.findViewById<EditText>(R.id.etDireccion).text.toString(),
                aforo = aforoStr.toInt(),
                foto = view.findViewById<EditText>(R.id.etFoto).text.toString(),
                categoria = spinner.selectedItem.toString(),
                id = TODO(),
                idAdmin = TODO()
            )

            enviarEvento(evento)
        }

        return view
    }

    private fun showDateTimePicker(onFechaLista: (String) -> Unit) {
        val c = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, day ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val fecha = String.format(
                    "%04d-%02d-%02dT%02d:%02d:00",
                    year, month + 1, day, hour, minute
                )
                onFechaLista(fecha)
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun enviarEvento(evento: Event) {
        lifecycleScope.launch {
            try {
                val response = ApiService.RetrofitClient.instance.crearEvento(evento)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Evento creado", Toast.LENGTH_SHORT).show()
                    onEventoCreado?.invoke()
                    dismiss()
                } else {
                    Toast.makeText(context, "Error ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

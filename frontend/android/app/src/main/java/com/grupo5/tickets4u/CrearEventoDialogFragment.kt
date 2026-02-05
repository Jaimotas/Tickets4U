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

class CrearEventoDialogFragment(
    val eventoParaEditar: Event? = null,
    val onEventoGuardado: () -> Unit
) : DialogFragment() {

    // Cambiamos a String? para que coincida con el modelo Event
    private var fechaInicioIso: String? = ""
    private var fechaFinIso: String? = ""

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

        val tvTitulo = view.findViewById<TextView>(R.id.tvTituloDialog)
        val btnGuardar = view.findViewById<Button>(R.id.btnCrear)
        val btnVolver = view.findViewById<ImageButton>(R.id.btnVolverDialog)
        val spinner = view.findViewById<Spinner>(R.id.spinnerCategoria)

        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etDesc = view.findViewById<EditText>(R.id.etDescripcion)
        val etCiudad = view.findViewById<EditText>(R.id.etCiudad)
        val etUbicacion = view.findViewById<EditText>(R.id.etUbicacion)
        val etDireccion = view.findViewById<EditText>(R.id.etDireccion)
        val etAforo = view.findViewById<EditText>(R.id.etAforo)
        val etFoto = view.findViewById<EditText>(R.id.etFoto)
        val fotoNombre = etFoto.text.toString()
            .substringBeforeLast('.') // elimina extensión si la hay
            .lowercase()             // minúsculas para que coincida con res/drawable
        val fotoResId = resources.getIdentifier(fotoNombre, "drawable", requireContext().packageName)


        val categorias = arrayOf("ACTUAL", "DESTACADO", "INTERNACIONAL")
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categorias)

        eventoParaEditar?.let { ev ->
            tvTitulo.text = "Editar Evento"
            btnGuardar.text = "FINALIZAR EDICIÓN"
            etNombre.setText(ev.nombre)
            etDesc.setText(ev.descripcion)
            etCiudad.setText(ev.ciudad)
            etUbicacion.setText(ev.ubicacion)
            etDireccion.setText(ev.direccion)
            etAforo.setText(ev.aforo?.toString() ?: "0")
            etFoto.setText(ev.foto)

            // Aquí ya no dará error porque ambos son String?
            fechaInicioIso = ev.fechaInicio
            fechaFinIso = ev.fechaFin

            val pos = categorias.indexOf(ev.categoria?.uppercase())
            if (pos >= 0) spinner.setSelection(pos)
        }

        btnVolver.setOnClickListener { dismiss() }

        view.findViewById<Button>(R.id.btnFechaInicio).setOnClickListener {
            showDateTimePicker { fecha -> fechaInicioIso = fecha }
        }

        view.findViewById<Button>(R.id.btnFechaFin).setOnClickListener {
            showDateTimePicker { fecha -> fechaFinIso = fecha }
        }

        btnGuardar.setOnClickListener {
            val aforoVal = etAforo.text.toString().toIntOrNull() ?: 0

            val eventoData = Event(
                id = eventoParaEditar?.id,
                nombre = etNombre.text.toString(),
                descripcion = etDesc.text.toString(),
                fechaInicio = fechaInicioIso,
                fechaFin = fechaFinIso,
                ciudad = view.findViewById<EditText>(R.id.etCiudad).text.toString(),
                ubicacion = view.findViewById<EditText>(R.id.etUbicacion).text.toString(),
                direccion = view.findViewById<EditText>(R.id.etDireccion).text.toString(),
                aforo = aforoVal.toInt(),
                foto = fotoNombre,
                categoria = spinner.selectedItem.toString(),
                idAdmin = TODO()

            )

            procesarEvento(eventoData)
        }

        return view
    }

    private fun showDateTimePicker(onFechaLista: (String) -> Unit) {
        val c = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val fecha = String.format("%04d-%02d-%02dT%02d:%02d:00", year, month + 1, day, hour, minute)
                onFechaLista(fecha)
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        DatePickerDialog(
            requireContext(),
            dateSetListener,
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun procesarEvento(evento: Event) {
        lifecycleScope.launch {
            try {
                val response = if (eventoParaEditar == null) {
                    ApiService.RetrofitClient.instance.crearEvento(evento)
                } else {
                    ApiService.RetrofitClient.instance.editarEvento(eventoParaEditar.id!!, evento)
                }

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "¡Éxito!", Toast.LENGTH_SHORT).show()
                    onEventoGuardado()
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
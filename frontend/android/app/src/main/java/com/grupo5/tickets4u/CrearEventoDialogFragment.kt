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

        // Referencias
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

        // Configurar Spinner
        val categorias = arrayOf("ACTUAL", "DESTACADO", "INTERNACIONAL")
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categorias)

        // MODO EDICIÓN: Cargar datos si existen
        eventoParaEditar?.let { ev ->
            tvTitulo.text = "Editar Evento"
            btnGuardar.text = "FINALIZAR EDICIÓN"
            etNombre.setText(ev.nombre)
            etDesc.setText(ev.descripcion)
            etCiudad.setText(ev.ciudad)
            etUbicacion.setText(ev.ubicacion)
            etDireccion.setText(ev.direccion)
            etAforo.setText(ev.aforo.toString())
            etFoto.setText(ev.foto)
            fechaInicioIso = ev.fechaInicio
            fechaFinIso = ev.fechaFin
            spinner.setSelection(categorias.indexOf(ev.categoria))
        }

        btnVolver.setOnClickListener { dismiss() }
        view.findViewById<Button>(R.id.btnFechaInicio).setOnClickListener { showDateTimePicker { fechaInicioIso = it } }
        view.findViewById<Button>(R.id.btnFechaFin).setOnClickListener { showDateTimePicker { fechaFinIso = it } }

        btnGuardar.setOnClickListener {
            val aforoVal = etAforo.text.toString().toIntOrNull() ?: 0

            val eventoData = Event(
                id = eventoParaEditar?.id, // Mantiene el ID si es edición, null si es nuevo
                nombre = etNombre.text.toString(),
                descripcion = etDesc.text.toString(),
                fechaInicio = fechaInicioIso,
                fechaFin = fechaFinIso,
                ciudad = etCiudad.text.toString(),
                ubicacion = etUbicacion.text.toString(),
                direccion = etDireccion.text.toString(),
                aforo = aforoVal,
                foto = etFoto.text.toString(),
                categoria = spinner.selectedItem.toString(),
                idAdmin = 1
            )

            lifecycleScope.launch {
                try {
                    val response = if (eventoParaEditar == null) {
                        RetrofitClient.instance.crearEvento(eventoData)
                    } else {
                        // Usamos !! porque en edición el ID nunca será nulo
                        RetrofitClient.instance.editarEvento(eventoParaEditar.id!!, eventoData)
                    }

                    if (response.isSuccessful) {
                        Toast.makeText(context, "¡Éxito!", Toast.LENGTH_SHORT).show()
                        onEventoGuardado()
                        dismiss()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
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
                val response = RetrofitClient.instance.crearEvento(evento)
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

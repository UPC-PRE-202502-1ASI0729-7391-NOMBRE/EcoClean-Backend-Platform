package pe.com.ecocleany.ecosmart.shared.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shared/districts")
@Tag(name = "Shared", description = "Recursos compartidos (Distritos, Config)")
public class DistrictsController {

    private static final List<String> LIMA_DISTRICTS = Arrays.asList(
            "Ancón", "Ate", "Barranco", "Breña", "Carabayllo", "Chaclacayo", "Chorrillos",
            "Cieneguilla", "Comas", "El Agustino", "Independencia", "Jesús María",
            "La Molina", "La Victoria", "Lima", "Lince", "Los Olivos", "Lurigancho",
            "Lurín", "Magdalena del Mar", "Miraflores", "Pachacámac", "Pucusana",
            "Pueblo Libre", "Puente Piedra", "Punta Hermosa", "Punta Negra", "Rímac",
            "San Bartolo", "San Borja", "San Isidro", "San Juan de Lurigancho",
            "San Juan de Miraflores", "San Luis", "San Martín de Porres", "San Miguel",
            "Santa Anita", "Santa María del Mar", "Santa Rosa", "Santiago de Surco",
            "Surquillo", "Villa El Salvador", "Villa María del Triunfo"
    );

    @Operation(summary = "Obtener lista oficial de distritos de Lima")
    @GetMapping
    public ResponseEntity<List<String>> getAllDistricts() {
        return ResponseEntity.ok(LIMA_DISTRICTS);
    }
}
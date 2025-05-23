package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.dto.DeductionDTO;
import com.mycompany.salary_management.service.DeductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/deductions")
public class DeductionController {

    @Autowired
    private DeductionService deductionService;

    private ResponseEntity<Map<String, Object>> buildResponse(boolean success, String message, Object data, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data); // HashMap permet les valeurs null
        return ResponseEntity.status(status).body(response);
    }

    //POST: Crée une nouvelle déduction
    @PostMapping
    public ResponseEntity<Map<String, Object>> addDeduction(@RequestBody DeductionDTO deduction) {
        try {
            DeductionDTO savedDeduction = deductionService.createDeduction(deduction);
            return buildResponse(true, "Déduction ajoutée avec succès", savedDeduction, HttpStatus.CREATED);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de l'ajout de la déduction: " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    // GET
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDeduction() {
        try {
            return buildResponse(true, "Déductions récupérées avec succès", deductionService.getAllDeduction(), HttpStatus.OK);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la récupération des déductions", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET : Déduction par id
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDeductionById(@PathVariable Long id) {
        try {
            DeductionDTO deduction = deductionService.getDeductionById(id);
            if (deduction != null) {
                return buildResponse(true, "Déduction récupérée avec succès", deduction, HttpStatus.OK);
            } else {
                return buildResponse(false, "Déduction non trouvée avec l'ID: " + id, null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la récupération de la déduction: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDeduction(@PathVariable Long id, @RequestBody DeductionDTO deduction) {
        try {
            DeductionDTO updatedDeduction = deductionService.updateDeduction(id, deduction);
            if (updatedDeduction != null) {
                return buildResponse(true, "Déduction mise à jour avec succès", updatedDeduction, HttpStatus.OK);
            } else {
                return buildResponse(false, "Déduction non trouvée avec l'ID: " + id, null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la mise à jour de la déduction: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDeduction(@PathVariable Long id) {
        try {
            if (!deductionService.existsById(id)) {
                return buildResponse(false, "Déduction non trouvée avec l'ID: " + id, null, HttpStatus.NOT_FOUND);
            }

            if (deductionService.isDeductionAssociatedWithPayroll(id)) {
                return buildResponse(false, "La déduction ne peut pas être supprimé car elle est déjà associée à une ou plusieurs fiches de paie", null, HttpStatus.CONFLICT);
            }

            deductionService.deleteDeduction(id);
            return buildResponse(true, "Déduction supprimée avec succès", null, HttpStatus.OK);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la suppression de la déduction: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.rplace.clicker.dto;

import com.rplace.clicker.model.Player;

/**
 * Objet de transfert de données pour le résultat d'un achat.
 * @param success Indique si l'achat a été validé.
 * @param player  L'objet Player (mis à jour en cas de succès).
 */
public record PurchaseResult(boolean success, Player player) {}

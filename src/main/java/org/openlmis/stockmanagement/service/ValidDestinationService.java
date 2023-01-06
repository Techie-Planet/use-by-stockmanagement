/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.stockmanagement.service;

import static org.openlmis.stockmanagement.i18n.MessageKeys.ERROR_DESTINATION_ASSIGNMENT_NOT_FOUND;
import static org.openlmis.stockmanagement.i18n.MessageKeys.ERROR_DESTINATION_NOT_FOUND;
import static org.openlmis.stockmanagement.i18n.MessageKeys.ERROR_NODE_NOT_FOUND;
import static org.slf4j.ext.XLoggerFactory.getXLogger;

import java.util.Optional;
import java.util.UUID;
import org.openlmis.stockmanagement.domain.sourcedestination.Node;
import org.openlmis.stockmanagement.domain.sourcedestination.ValidDestinationAssignment;
import org.openlmis.stockmanagement.dto.ValidSourceDestinationDto;
import org.openlmis.stockmanagement.exception.ResourceNotFoundException;
import org.openlmis.stockmanagement.exception.ValidationMessageException;
import org.openlmis.stockmanagement.repository.NodeRepository;
import org.openlmis.stockmanagement.repository.ValidDestinationAssignmentRepository;
import org.openlmis.stockmanagement.util.Message;
import org.slf4j.ext.XLogger;
import org.slf4j.profiler.Profiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

@Service
public class ValidDestinationService extends SourceDestinationBaseService {

  private static final XLogger XLOGGER = getXLogger(ValidDestinationService.class);

  @Autowired
  private ValidDestinationAssignmentRepository validDestinationRepository;
  @Autowired
  private NodeRepository nodeRepository;

  /**
   * Find valid sources page by program ID and facility type ID.
   *
   * @param programId program ID
   * @param facilityId facility ID
   * @param pageable pagination and sorting parameters
   * @return valid source assignment DTOs
   */
  public Page<ValidSourceDestinationDto> findDestinations(UUID programId,
                                                          UUID facilityId, Pageable pageable) {
    XLOGGER.entry();
    Profiler profiler = new Profiler("FIND_DESTINATION_ASSIGNMENTS");
    profiler.setLogger(XLOGGER);

    Page<ValidSourceDestinationDto> assignments =
            findAssignments(programId, facilityId, validDestinationRepository, profiler, pageable);
    profiler.stop().log();
    XLOGGER.exit();
    return assignments;
  }

  /**
   * Assign a destination to a program and facility type.
   *
   * @param assignment assignment JPA model
   * @return a valid source destination dto
   */
  public ValidSourceDestinationDto assignDestination(ValidDestinationAssignment assignment) {
    assignment.setId(null);
    return doAssign(assignment, ERROR_DESTINATION_NOT_FOUND, validDestinationRepository);
  }

  /**
   * Find existing destination assignment.
   *
   * @param assignment assignment JPA model
   * @return a valid source destination dto
   */
  //  @Transactional
  public ValidSourceDestinationDto findByProgramFacilityDestination(
      ValidDestinationAssignment assignment) {
    return findAssignment(assignment, validDestinationRepository);
  }

  /**
   * Find existing destinations.
   *
   * @param assignmentId destination assignment Id
   * @return assigmnet dto
   * @throws ValidationMessageException when assignment was not found
   */
  public ValidSourceDestinationDto findById(UUID assignmentId) {
    return findById(assignmentId, validDestinationRepository,
        ERROR_DESTINATION_ASSIGNMENT_NOT_FOUND);
  }

  /**
   * Delete a destination assignment by Id.
   *
   * @param assignmentId destination assignment Id
   */
  public void deleteDestinationAssignmentById(UUID assignmentId) {
    doDelete(assignmentId, validDestinationRepository, ERROR_DESTINATION_ASSIGNMENT_NOT_FOUND);
  }

  /**
   * Find a Reference Facility ID from a Node ID.
   *
   * @param nodeId the Node ID
   * @return UUID.
   * @throws ResourceNotFoundException when node not found.
   */
  public Node getReferenceIdFromNodeId(UUID nodeId) {
    Optional<Node> node = nodeRepository.findById(nodeId);
    if (node.isPresent()) {
      return node.get();
    }
    throw new ResourceNotFoundException(ERROR_NODE_NOT_FOUND);
  }

}

/**
 * Copyright (c) 2017, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above
 *     	 copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 *     * The names "German Federal Agency for Cartography and Geodesy",
 *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE",
 *       "GDI-DE Registry" and the names of other contributors must not
 *       be used to endorse or promote products derived from this
 *       software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN
 * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 *
 * =====================================================================
 *
 * Integration into the ISO Geodetic Registry was developed by
 * Natural Resources Canada.
 *
 * TODO Add license
 *
 * Copyright (c) 2020, Natural Resources Canada
 */
package org.iso.registry.client.controller;

import javax.servlet.http.HttpServletRequest;

import org.iso.registry.api.registry.RegistryFixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.geoinfoffm.registry.api.UserRegistrationException;
import de.geoinfoffm.registry.client.web.AbstractController;
import de.geoinfoffm.registry.core.ItemClassRegistry;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.persistence.ItemClassRepository;

/**
 * Controller class for methods fixing registry content
 *
 * @author Florian Esser
 *
 */
@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/fix")
public class FixController extends AbstractController
{
	@Autowired
	private RegistryConfiguration registryConfiguration;

	@Autowired
	private RegistryFixService fixer;

	private static boolean isFixing = false;

	private StringBuilder initLog;

	/**
	 * Create the controller
	 */
	@Autowired
	public FixController(ItemClassRepository itemClassRepository, ItemClassRegistry itemClassRegistry) {
		super();
	}

	/**
	 * Retrieve the status of the current fix operation
	 *
	 * @param request The underlying request
	 * @return <code>204 (No content)</code> if no fixing operation is ongoing,
	 *         <code>206 (Partial content)</code> if the current fixing is still ongoing,
	 *         or <code>200 (OK)</code> if the fix operation is finished.
	 * @throws UnauthorizedException
	 */
	@RequestMapping(value = "/{fix}/status", method = RequestMethod.GET)
	public ResponseEntity<String> fixStatus(HttpServletRequest request) throws UnauthorizedException {
		if (initLog == null) {
			return new ResponseEntity<String>("Not currently fixing.", HttpStatus.NO_CONTENT);
		}
		else if (isFixing) {
			return new ResponseEntity<String>(initLog.toString(), HttpStatus.PARTIAL_CONTENT);
		}
		else {
			return new ResponseEntity<String>(initLog.toString(), HttpStatus.OK);
		}
	}

	/**
	 * Show the fix form
	 *
	 * @param request The underlying request
	 * @param fix     The fix to apply
	 * @param model   The model
	 * @return The name of the view to display next
	 * @throws InvalidProposalException
	 * @throws UserRegistrationException
	 * @throws UnauthorizedException
	 */
	@RequestMapping(value = "/{fix}", method = RequestMethod.GET)
	public String fix(HttpServletRequest request, @PathVariable("fix") String fix, Model model)
			throws InvalidProposalException, UserRegistrationException, UnauthorizedException {
		model.addAttribute("fix", fix);

		return "fix";
	}

	/**
	 * Start a fix operation
	 *
	 * @param request The underlying request
	 * @param fix     The fix to apply
	 * @return The status message
	 * @throws Exception
	 */
	@Async
	@Transactional
	@RequestMapping(value = "/{fix}/start", method = RequestMethod.GET)
	public ResponseEntity<String> fixStart(HttpServletRequest request, @PathVariable("fix") String fix) throws Exception {
		if (StringUtils.isEmpty(fix)) {
			return new ResponseEntity<String>("Missing 'fix'", HttpStatus.BAD_REQUEST);
		}

		HttpStatus status = HttpStatus.OK;
		String message = "";
		try {
			if (!isFixing) {
				isFixing = true;
				initLog = new StringBuilder();

				boolean mailEnabled = registryConfiguration.isMailEnabled();
				registryConfiguration.setMailEnabled(false);

				switch (fix) {
					case "create-extent-item-class":
						fixer.createExtentItemClass(initLog);
						break;
					case "migrate-operations":
						fixer.migrateCoordinateOperationExtents(initLog);
						break;
					default:
						message = String.format("Unknown fix: '%s'", fix);
						initLog.append(message);
						status = HttpStatus.NOT_FOUND;
				}

				initLog.append("\nFinished.");
				isFixing = false;

				registryConfiguration.setMailEnabled(mailEnabled);
			}
		}
		finally {
			isFixing = false;
		}

		return new ResponseEntity<String>(message, status);
	}
}

package com.cscie97.store.model;

import com.cscie97.ledger.LedgerException;
import com.cscie97.ledger.LedgerInterface;
import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.AuthenticationServiceInterface;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.controller.ObserverException;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The CommandProcessor processes a file
 * of commands or an individual command string
 * and passes the parsed commands to the Store Model Service API
 *
 * Uses the store model service API and the ledger service API to complete the commands
 * passed to the command processor
 */
public class CommandProcessor {
	/**
	 * Processes commands from an input file
	 * @param file_path the address of the file containing
	 *                  commands
	 * @param storeModelServiceInterface store model service interface to use to call the commands
	 * @param ledgerInterface ledger service to create accounts using
	 * @param authService authentication service to control access
	 * @throws CommandProcessorException
	 */
	public static void processCommandFile(File file_path,
										  StoreModelServiceInterface storeModelServiceInterface,
										  LedgerInterface ledgerInterface,
										  AuthenticationServiceInterface authService) throws CommandProcessorException {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file_path))) {
			String command = bufferedReader.readLine();
			int lineNumber = 0;
			while (command != null) {
				lineNumber ++;
				System.out.println(command);
				if (!command.startsWith("#") && command.length() > 0) {
					try {
						processCommand(command, storeModelServiceInterface, ledgerInterface, authService);
					} catch (CommandProcessorException e) {
						throw new CommandProcessorException(e.getAction(), e.getError(), lineNumber);
					}
				}
				command = bufferedReader.readLine();
			}
		} catch (IOException e) {
			throw new CommandProcessorException("process from file", "IO exception");
		}
	}

	/**
	 * Parses a single command and performs the appropriate action
	 * @param command single command to be parsed
	 * @param storeModelServiceInterface store model service API to use to complete commands
	 * @param ledgerInterface ledger service API to use for ledger commands
	 * @param authService authentication service API
	 * @throws CommandProcessorException
	 */
	private static void processCommand(String command,
									   StoreModelServiceInterface storeModelServiceInterface,
									   LedgerInterface ledgerInterface,
									   AuthenticationServiceInterface authService) throws CommandProcessorException {
		ArrayList<String> args = new ArrayList<>();
		Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
		Matcher regexMatcher = regex.matcher(command);

		while (regexMatcher.find()) {
			args.add(regexMatcher.group().toLowerCase());
		}

		String action = args.get(0);
		String subAction = args.get(1);
		String fullAction;
		String[] location;

		if (!action.contains("_")) {
			fullAction = action + " " + subAction;
		} else {
			fullAction = action;
		}
		try {
			switch (fullAction) {
				// store model service commands
				case ("add_basket_item"):
					storeModelServiceInterface.updateCustomerBasket(
							args.get(1),
							args.get(3),
							Integer.parseInt(args.get(5)),
							"adminpassword"); // placeholder until assignment 3
					break;
				case ("clear_basket"):
					storeModelServiceInterface.clearBasket(args.get(1),
							"adminpassword");
					break;
				case ("create command"):
					storeModelServiceInterface.createCommand(args.get(2), args.get(4), args.get(6),
							"adminpassword");
					break;
				case ("create event"):
					storeModelServiceInterface.createEvent(
							args.get(2),
							args.get(4),
							args.get(6).replace("\"", ""),
							"adminpassword");
					break;
				case ("define aisle"):
					storeModelServiceInterface.defineAisle(
							args.get(2).split(":")[0],
							args.get(2).split(":")[1],
							args.get(4),
							args.get(6),
							args.get(8),
							"adminpassword");
					break;
				case ("define customer"):
					storeModelServiceInterface.defineCustomer(
							args.get(2),
							args.get(4),
							args.get(6),
							args.get(8),
							args.get(10),
							args.get(12),
							"adminpassword"
					);
					break;
				case ("define device"):
					location = args.get(8).split(":");
					storeModelServiceInterface.defineDevice(
							args.get(2),
							args.get(4),
							args.get(6),
							location[0],
							location[1],
							"adminpassword"
					);
					break;
				case ("define inventory"):
					location = args.get(4).split(":");
					storeModelServiceInterface.defineInventory(
							args.get(2),
							location[0],
							location[1],
							location[2],
							Integer.parseInt(args.get(6)),
							Integer.parseInt(args.get(8)),
							args.get(10),
							"adminpassword"
					);
					break;
				case ("define product"):
					if (args.size() > 14) {
						storeModelServiceInterface.defineProduct(
								args.get(2),
								args.get(4),
								args.get(6),
								Float.parseFloat(args.get(8)),
								args.get(10),
								Float.parseFloat(args.get(12)),
								args.get(14),
								"adminpassword"
						);
					} else {
						storeModelServiceInterface.defineProduct(
								args.get(2),
								args.get(4),
								args.get(6),
								Float.parseFloat(args.get(8)),
								args.get(10),
								Float.parseFloat(args.get(12)),
								"adminpassword"
						);
					}
					break;
				case ("define shelf"):
					location = args.get(2).split(":");
					if (args.size() > 10) {
						storeModelServiceInterface.defineShelf(
								location[0],
								location[1],
								location[2],
								args.get(4),
								args.get(6),
								args.get(8),
								args.get(10),
								"adminpassword"
						);
					} else {
						storeModelServiceInterface.defineShelf(
								location[0],
								location[1],
								location[2],
								args.get(4),
								args.get(6),
								args.get(8),
								"adminpassword"
						);
					}
					break;
				case ("define store"):
					storeModelServiceInterface.defineStore(args.get(2), args.get(3), args.get(4),
							"adminpassword");
					break;
				case ("get_customer_basket"):
					storeModelServiceInterface.getCustomerBasket(args.get(1),
							"adminpassword");
					break;
				case ("remove_basket_item"):
					storeModelServiceInterface.updateCustomerBasket(
							args.get(1),
							args.get(3),
							-1* Integer.parseInt(args.get(5)),
							"adminpassword");
					break;
				case ("show aisle"):
					location = args.get(2).split(":");
					storeModelServiceInterface.showAisle(location[0], location[1],
							"adminpassword");
					break;
				case ("show_basket_items"):
					storeModelServiceInterface.showBasketItems(args.get(1),
							"adminpassword");
					break;
				case ("show customer"):
					storeModelServiceInterface.showCustomer(args.get(2),
							"adminpassword");
					break;
				case ("show device"):
					storeModelServiceInterface.showDevice(args.get(2), args.get(4),
							"adminpassword");
					break;
				case ("show inventory"):
					storeModelServiceInterface.showInventory(args.get(2), args.get(4),
							"adminpassword");
					break;
				case ("show product"):
					storeModelServiceInterface.showProduct(args.get(2),
							"adminpassword");
					break;
				case ("show shelf"):
					location = args.get(2).split(":");
					storeModelServiceInterface.showShelf(location[0], location[1], location[2],
							"adminpassword");
					break;
				case ("show store"):
					storeModelServiceInterface.showStore(args.get(2),
							"adminpassword");
					break;
				case ("update customer"):
					storeModelServiceInterface.updateCustomer(
							args.get(2),
							args.get(4).split(":")[0],
							args.get(4).split(":")[1],
							"adminpassword");
					break;
				case ("update inventory"):
					storeModelServiceInterface.updateInventory(
							args.get(2),
							args.get(4),
							Integer.parseInt(args.get(6)),
							"adminpassword");
					break;

				// ledger service commands
				case ("create account"):
					String accountId = ledgerInterface.createAccount(args.get(2));
					System.out.println("account with ID " + accountId + " created");
					break;
				case ("process transaction"): // creates and submits new transaction based on inputs
					ledgerInterface.addTransaction(
											args.get(2),
											Integer.parseInt(args.get(4)),
											Integer.parseInt(args.get(6)),
											args.get(8),
											args.get(10),
											args.get(12));
					System.out.println("transaction with ID " + args.get(2) + " processed");
					break;

				// authentication service commands
				case ("define permission"):
					authService.definePermission(
							args.get(2),
							args.get(3),
							args.get(4)
					);
					break;
				case("define role"):
					authService.defineRole(
							args.get(2),
							args.get(3),
							args.get(4)
					);
					break;
				case("define resource_role"):
					authService.defineResourceRole(
							args.get(2),
							args.get(3),
							args.get(4),
							args.get(5),
							args.get(6)
					);
					break;
				case("define resource"):
					authService.defineResource(
							args.get(2),
							args.get(3)
					);
					break;
				case("add_permission_to_role"):
					authService.addRoleEntitlement(
							args.get(1),
							args.get(2)
					);
					break;
				case("create user"):
					authService.createUser(
							args.get(2),
							args.get(3)
					);
					break;
				case("add_user_login"):
					authService.addUserLogin(
							args.get(1),
							args.get(2),
							args.get(3)
					);
					break;
				case("add_user_bio_print"):
					authService.addUserPrint(
							args.get(1),
							args.get(2)
					);
					break;
				case("add_role_to_user"):
					authService.addUserEntitlement(
							args.get(1),
							args.get(2)
					);
					break;
				case("user login"):
					System.out.println(authService.userLogin(
							args.get(2),
							args.get(3),
							args.get(4)
					));
					break;
				case("show auth"):
					authService.show();
					break;
				default:
					throw new CommandProcessorException(command, "unrecognized command");
			}
		} catch (StoreModelServiceException | ObserverException | LedgerException | AccessDeniedException | InvalidAuthTokenException | NoSuchAlgorithmException e){
			System.out.println(e); // print errors and continue
		}
	}
}


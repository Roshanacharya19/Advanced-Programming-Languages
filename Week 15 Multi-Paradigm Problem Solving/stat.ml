(* Statistics calculator using functional programming principles *)

(* Safely compute average of a list *)
let mean list =
  let rec sum_and_count lst acc count =
    match lst with
    | [] -> (acc, count)
    | head :: tail -> sum_and_count tail (acc + head) (count + 1)
  in
  let (total, count) = sum_and_count list 0 0 in
  if count = 0 then
    failwith "Cannot compute mean of empty list"
  else
    float_of_int total /. float_of_int count

(* Calculate median using a pure functional approach *)
let median list =
  let sorted = List.sort compare list in
  let length = List.length sorted in
  if length = 0 then
    failwith "Cannot compute median of empty list"
  else if length mod 2 = 1 then
    float_of_int (List.nth sorted (length / 2))
  else
    let mid_right = length / 2 in
    let mid_left = mid_right - 1 in
    float_of_int (List.nth sorted mid_left + List.nth sorted mid_right) /. 2.0

(* Count occurrences of elements in list *)
let count_occurrences list =
  let rec add_to_counts element counts =
    match counts with
    | [] -> [(element, 1)]
    | (e, count) :: rest when e = element -> (e, count + 1) :: rest
    | pair :: rest -> pair :: add_to_counts element rest
  in
  let rec count lst counts =
    match lst with
    | [] -> counts
    | head :: tail -> count tail (add_to_counts head counts)
  in
  count list []

(* Find the mode(s) - most frequent element(s) *)
let mode list =
  if List.length list = 0 then
    failwith "Cannot compute mode of empty list"
  else
    let frequencies = count_occurrences list in
    
    (* Find the maximum frequency *)
    let max_frequency = 
      List.fold_left (fun acc (_, count) -> max acc count) 0 frequencies
    in
    
    (* Filter elements with the maximum frequency *)
    List.filter_map 
      (fun (element, count) -> 
        if count = max_frequency then Some element else None) 
      frequencies

(* Function to read integers from user input *)
let read_integers () =
  print_string "Enter integers separated by spaces: ";
  flush stdout;
  let input_line = read_line () in
  let tokens = String.split_on_char ' ' input_line in
  List.filter_map 
    (fun token -> 
      try Some (int_of_string token)
      with Failure _ -> None)
    tokens

(* Main program using functional composition *)
let () =
  try
    let numbers = read_integers () in
    if List.length numbers = 0 then
      print_endline "No valid integers were entered"
    else begin
      Printf.printf "Mean: %.2f\n" (mean numbers);
      Printf.printf "Median: %.2f\n" (median numbers);
      
      let modes = mode numbers in
      print_string "Mode(s): ";
      List.iter (fun m -> Printf.printf "%d " m) modes;
      print_newline ()
    end
  with
  | Failure msg -> print_endline ("Error: " ^ msg)
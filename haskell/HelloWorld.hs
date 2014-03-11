
--Prints Hello World, then prints the odd & even numbers between 0 and 100.
main = do
	putStrLn "Hello World"

	--This commented section prints the list of odd/even numbers less than 100
	--	but the printed list is a list of strings of said numbers
	--let evens = map show (takeWhile (<100) (map (*2) [0..]))
	--let odds = map show (takeWhile (<100) (map (\x -> 2*x + 1) [0..]))
	--putStrLn $ show odds
	--putStrLn $ show evens

	--This version is slightly more readable, and returns a list of Num
	let odds1 = findNumbersLessThanFittingFunction 100 (\x -> 2*x + 1)
	let evens1 = findNumbersLessThanFittingFunction 100 (\x -> 2*x)
	putStrLn $ show odds1
	putStrLn $ show evens1
	putStrLn . show . removeDuplicates $ [0..10] ++ [0..10]


findNumbersLessThanFittingFunction::(Num a, Ord a, Enum a) => a->(a->a)->[a]
findNumbersLessThanFittingFunction limit fn = 
	let functionedElems = map fn [0 ..];
		underLimit = takeWhile (<limit) functionedElems;
	in underLimit --underLimit is the return list

--Simple implementation of removing the duplicate elements from a list
--	could also be done with calling "nub" after including
--	import Data.List (nub)
removeDuplicates::(Eq a) => [a] -> [a]
removeDuplicates xs = 
	foldr (\x acc -> if (x `elem` acc) then acc else x:acc) [] xs
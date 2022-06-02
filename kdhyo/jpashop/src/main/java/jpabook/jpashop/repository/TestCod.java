package jpabook.jpashop.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestCod {

	public static void main(String[] args) {

	}

	public int[] solution(String[] id_list, String[] report, int k) {
		List<Integer> result = new ArrayList<>();
		Map<String, Set<String>> idListResult = new LinkedHashMap<>();
		Map<String, Set<String>> reportResult = new HashMap<>();

		for (String id : id_list) {
			idListResult.put(id, new HashSet<>());
		}

		for (String id : report) {
			String[] ids = id.split(" ");

			reportResult.getOrDefault(ids[1], new HashSet<>()).add(ids[0]);
			idListResult.getOrDefault(ids[0], new HashSet<>()).add(ids[1]);
		}

		idListResult.forEach((key, value) -> {
			int count = (int) value.stream()
				.filter(id -> reportResult.get(id).size() >= k)
				.count();
			result.add(count);
		});

		return result.stream().mapToInt(Integer::intValue).toArray();
	}

}

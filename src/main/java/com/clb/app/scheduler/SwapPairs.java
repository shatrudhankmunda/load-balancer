package com.clb.app.scheduler;

import java.util.Arrays;

public class SwapPairs {
    public static void main(String[] args) {
        int[] nums = {1, 3, 4, 6, 5, 2};
        Arrays.sort(nums); // Sort the array first
        for (int i = 0; i < nums.length - 1; i += 2) {
            // Swap nums[i] and nums[i + 1]
            int temp = nums[i];
            nums[i] = nums[i + 1];
            nums[i + 1] = temp;
        }

        // Print result
        for (int num : nums) {
            System.out.print(num + " ");
        }
    }
}

import 'package:flutter/material.dart';

class AnimatedFilterChip extends StatelessWidget {
  final bool selected;
  final VoidCallback onClick;
  final String label;
  final Color selectedColor;

  const AnimatedFilterChip({
    Key? key,
    required this.selected,
    required this.onClick,
    required this.label,
    required this.selectedColor,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: AnimatedScale(
        scale: selected ? 1.05 : 1.0,
        duration: const Duration(milliseconds: 200),
        curve: Curves.easeInOut,
        child: FilterChip(
          selected: selected,
          onSelected: (_) => onClick(),
          label: SizedBox(
            width: double.infinity,
            child: Text(
              label,
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 13,
                fontWeight: selected ? FontWeight.bold : FontWeight.normal,
                color: selected ? Colors.white : const Color(0xFF6B7280),
              ),
            ),
          ),
          backgroundColor: const Color(0xFFE5E7EB),
          selectedColor: selectedColor,
          checkmarkColor: Colors.white,
          showCheckmark: false,
          padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 8),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
            side: BorderSide.none,
          ),
        ),
      ),
    );
  }
}

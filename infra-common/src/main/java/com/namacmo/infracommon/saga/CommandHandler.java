package com.namacmo.infracommon.saga;

/**
 * {@code T}는 명령(Command)이 어떤 도메인 객체에 영향을 미쳤는지를 나타내는 제네릭 타입입니다.
 *
 * 예를 들어, 사용자가 생성됨에 따라 {@code MembershipLevel} 도메인 객체가 변경된다면,
 * {@code T}는 {@code MembershipLevel}이 됩니다.
 *
 * @param <T> 명령에 의해 변경되는 도메인 객체의 타입
 */

public interface CommandHandler<T> {
}
